package com.example.controllbuilding.model.DTO;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String login;
    private String password;
    private String role;
}