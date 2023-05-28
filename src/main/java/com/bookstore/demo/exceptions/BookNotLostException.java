package com.bookstore.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookNotLostException extends RuntimeException {
    public BookNotLostException(String message) {
        super(message);
    }
}
