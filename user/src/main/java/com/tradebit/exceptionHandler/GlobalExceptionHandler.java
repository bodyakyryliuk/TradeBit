package com.tradebit.exceptionHandler;

import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.JwtNotValidException;
import com.tradebit.exception.UserAlreadyExistsException;
import com.tradebit.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtNotValidException.class)
    public ResponseEntity<Map<String, String>> handleJwtNotValidException(JwtNotValidException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
