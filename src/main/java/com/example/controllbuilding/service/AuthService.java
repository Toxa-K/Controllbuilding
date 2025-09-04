package com.example.controllbuilding.service;

import com.example.controllbuilding.config.JwtUtils;
import com.example.controllbuilding.model.DTO.LoginRequest;
import com.example.controllbuilding.model.DTO.RegisterRequest;
import com.example.controllbuilding.model.entity.UserEntity;
import com.example.controllbuilding.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        UserEntity user = userRepository.findByLogin(request.getLogin())
                .orElseThrow();
        return jwtUtils.generateToken(user);
    }

    public String register(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "User registered!";
    }
}