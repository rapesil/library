package com.bookstore.demo.exceptions;

import com.bookstore.demo.entities.BookStatus;

public class LossResponse {
    private String message;
    private BookStatus status;

    public LossResponse(String message, BookStatus newStatus) {
        this.message = message;
        this.status = newStatus;
    }

    public String getMessage() {
        return message;
    }

    public BookStatus getStatus() {
        return status;
    }
}