package com.bookstore.demo.controller.response;

import com.bookstore.demo.entities.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LossResponse {
    private String message;
    private BookStatus status;
}