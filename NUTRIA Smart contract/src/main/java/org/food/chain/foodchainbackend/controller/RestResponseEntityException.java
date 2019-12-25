package org.food.chain.foodchainbackend.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<Object> handleIdConvertException(NumberFormatException exception,
                                                           WebRequest webRequest) {
        return new ResponseEntity<>("Id has to be a number.",
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleIntegrityConstraintException(DataIntegrityViolationException exception,
                                                                     WebRequest webRequest) {
        return new ResponseEntity<>("Data Integrity Constraint Violated.",
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
