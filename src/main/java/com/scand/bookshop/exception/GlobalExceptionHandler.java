package com.scand.bookshop.exception;

import java.util.NoSuchElementException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final String PROPERTY_VALUE_EXCEPTION_MESSAGE = "Field values are not valid";
    private final String ARGUMENT_NOT_VALID = "Check the entered data";
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> propertyValueException(PropertyValueException e) {
        return ResponseEntity.badRequest().body(PROPERTY_VALUE_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> UserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<String> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ARGUMENT_NOT_VALID);
    }

    @ExceptionHandler(org.springframework.security.authentication.LockedException.class)
    public ResponseEntity<String> LockedException(LockedException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }
}