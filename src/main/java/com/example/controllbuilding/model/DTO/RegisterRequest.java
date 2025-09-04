package com.example.controllbuilding.model.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String login;
    private String password;
}
