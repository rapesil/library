package com.bookstore.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAlreadyRegistered extends RuntimeException{
    public BookAlreadyRegistered(String message) {
        super(message);
    }
}
