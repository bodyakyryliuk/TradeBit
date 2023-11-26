package com.tradebit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BinanceLinkException.class)
    public ResponseEntity<Map<String, String>> handleBinanceLinkException(BinanceLinkException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<Map<String, String>> handleInvalidQuantityException(InvalidQuantityException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSideException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSideException(InvalidSideException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSymbolException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSymbolException(InvalidSymbolException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTypeException(InvalidTypeException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedException(UnexpectedException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }

}
