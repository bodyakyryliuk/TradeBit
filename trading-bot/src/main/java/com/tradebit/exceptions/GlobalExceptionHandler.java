package com.tradebit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BotNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBotNotFoundException(BotNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
