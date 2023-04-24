package com.bookstore.demo.controller;

import com.bookstore.demo.entities.dto.UserDTO;
import com.bookstore.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return service.createUser(userDTO);
    }

}