package com.example.controllbuilding.mapper;

import com.example.controllbuilding.model.DTO.User;
import com.example.controllbuilding.model.entity.UserEntity;

public class UserMapper {
    public static User toDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        User dto = new User();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    public static UserEntity toEntity(User dto) {
        if (dto == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
