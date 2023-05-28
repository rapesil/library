package com.bookstore.demo.exceptions;

public class LossRecordExistsException extends RuntimeException {
    public LossRecordExistsException(String message) {
        super(message);
    }
}
