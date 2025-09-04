package com.example.controllbuilding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.util.TrustManagerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FTPService {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.connect-timeout:10000}")
    private int connectTimeout;



    public void uploadFile(String remotePath, InputStream inputStream) throws IOException {
        log.info("Начало загрузки файла на путь: {}", remotePath);
        FTPClient ftpClient = openConnection();
        log.info("Вернули ftpsClient...");
        try {
            // Убедимся, что директория существует
            String dirPath = remotePath.substring(0, remotePath.lastIndexOf('/'));
            log.info("Проверка/создание директории: {}", dirPath);
            ensureDirectoryExists(ftpClient, dirPath);

            // Попытка открыть поток передачи файла
            log.info("Открытие потока для передачи файла в: {}", remotePath);
            OutputStream outputStream = ftpClient.storeFileStream(remotePath);

            if (outputStream == null) {
                String reply = ftpClient.getReplyString();
                log.info("storeFileStream вернул null. FTP ответ: {}", reply);
                throw new IOException("Не удалось открыть поток для передачи файла: " + reply);
            }

            try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(outputStream)) {
                log.info("Поток открыт. Начинаем передачу данных...");
                inputStream.transferTo(bufferedOutput);
                log.info("Поток открыт. inputStream.transferTo(bufferedOutput);");
                bufferedOutput.flush();
                log.info("Передача данных завершена. Завершаем FTP-команду...");
            }

            // Завершаем команду передачи файла
            if (!ftpClient.completePendingCommand()) {
                String reply = ftpClient.getReplyString();
                log.info("Ошибка при завершении передачи файла. Ответ FTP-сервера: {}", reply);
                throw new IOException("Ошибка при завершении передачи файла: " + reply);
            }

            log.info("Файл успешно загружен на путь: {}", remotePath);

        } catch (IOException e) {
            log.info("Ошибка при загрузке файла на {}: {}", remotePath, e.getMessage(), e);
            throw e;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.info("Ошибка при закрытии inputStream: {}", e.getMessage(), e);
            }

            if (ftpClient.isConnected()) {
                try {
                    log.info("Отключение от FTP-сервера...");
                    ftpClient.logout();
                    ftpClient.disconnect();
                    log.info("Отключение выполнено успешно.");
                } catch (IOException e) {
                    log.info("Ошибка при отключении от FTP-сервера: {}", e.getMessage(), e);
                }
            }
        }
    }





    public FTPClient openConnection() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultTimeout(30000);
        ftpClient.setConnectTimeout(30000);
        ftpClient.setDataTimeout(30000);
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
        ftpClient.setConnectTimeout(connectTimeout);
        ftpClient.connect(host, port);

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new IOException("FTP отказал в соединении: " + ftpClient.getReplyString());
        }
        ftpClient.enterLocalPassiveMode();  // Очень важно для обхода проблем с NAT/firewall

        if (!ftpClient.login(username, password)) {
            throw new IOException("Ошибка входа: " + ftpClient.getReplyString());
        }

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        return ftpClient;
    }




    public InputStream downloadFile(String ftpPath) throws IOException {
        log.info("Загрузка файла с FTP: {}", ftpPath);
        FTPClient ftpClient = openConnection();

        InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
        if (inputStream == null || !FTPReply.isPositivePreliminary(ftpClient.getReplyCode())) {
            ftpClient.logout();
            ftpClient.disconnect();
            throw new FileNotFoundException("Файл не найден: " + ftpPath);
        }

        return new InputStream() {
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return inputStream.read(b, off, len);
            }

            @Override
            public void close() throws IOException {
                try {
                    inputStream.close();
                    ftpClient.completePendingCommand();
                } finally {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                }
            }
        };
    }
    private void ensureDirectoryExists(FTPClient ftpClient, String dirPath) throws IOException {
        if (ftpClient.changeWorkingDirectory(dirPath)) return;

        String[] dirs = dirPath.split("/");
        StringBuilder path = new StringBuilder();

        for (String dir : dirs) {
            if (dir.isEmpty()) continue;
            path.append("/").append(dir);
            if (!ftpClient.changeWorkingDirectory(path.toString())) {
                if (!ftpClient.makeDirectory(path.toString())) {
                    throw new IOException("Не удалось создать директорию: " + path);
                }
            }
        }

        if (!ftpClient.changeWorkingDirectory(dirPath)) {
            throw new IOException("Не удалось перейти в директорию после создания: " + dirPath);
        }
    }
}