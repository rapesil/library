package com.bookstore.demo.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

}

