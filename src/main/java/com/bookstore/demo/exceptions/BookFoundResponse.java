package com.bookstore.demo.exceptions;

import com.bookstore.demo.entities.BookStatus;

public class BookFoundResponse {
    private String message;
    private BookStatus status;

    public BookFoundResponse(String message, BookStatus newStatus) {
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