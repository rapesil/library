package com.bookstore.demo.exceptions;

public class BookAlreadyRegistered extends RuntimeException{
    public BookAlreadyRegistered(String message) {
        super(message);
    }
}
