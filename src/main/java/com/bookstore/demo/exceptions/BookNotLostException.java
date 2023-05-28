package com.bookstore.demo.exceptions;

public class BookNotLostException extends RuntimeException {
    public BookNotLostException(String message) {
        super(message);
    }
}
