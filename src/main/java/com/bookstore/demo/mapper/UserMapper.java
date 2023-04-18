package com.bookstore.demo.mapper;

import com.bookstore.demo.entities.User;
import com.bookstore.demo.entities.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
