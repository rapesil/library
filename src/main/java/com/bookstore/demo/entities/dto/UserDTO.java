package com.bookstore.demo.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record UserDTO(String name, String email) {
    public UserDTO() {
        this(null, null);
    }
}
