package com.bookstore.demo.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookControllerAdvice {

    @ExceptionHandler(BookAlreadyRegistered.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBookAlreadyRegisteredException(ErrorResponse ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(LossRecordExistsException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleLossRecordExistsException(LossRecordExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}



