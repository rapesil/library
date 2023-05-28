package com.bookstore.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LossRecordExistsException extends RuntimeException {
    public LossRecordExistsException(String message) {
        super(message);
    }
}
