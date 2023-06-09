package com.bookstore.demo.service;

import com.bookstore.demo.entities.User;
import com.bookstore.demo.entities.UserStatus;
import com.bookstore.demo.entities.dto.UserDTO;
import com.bookstore.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setStatus(UserStatus.APPROVED);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
