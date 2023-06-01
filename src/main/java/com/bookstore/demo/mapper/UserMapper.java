package com.bookstore.demo.mapper;

import com.bookstore.demo.entities.User;
import com.bookstore.demo.entities.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
            user.getName(),
            user.getEmail()
        );
    }
}
