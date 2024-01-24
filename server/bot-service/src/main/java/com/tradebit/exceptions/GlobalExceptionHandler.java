package com.tradebit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BotNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleBotNameAlreadyExistsException(BotNameAlreadyExistsException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BotNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBotNotFoundException(BotNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BuyOrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBuyOrderNotFoundException(BuyOrderNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CurrentPriceFetchException.class)
    public ResponseEntity<Map<String, String>> handleCurrentPriceFetchException(CurrentPriceFetchException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomServerException.class)
    public ResponseEntity<Map<String, String>> handleCustomServerException(CustomServerException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatusCode.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(CustomClientException.class)
    public ResponseEntity<Map<String, String>> handleCustomClientException(CustomClientException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(HighestPriceFetchException.class)
    public ResponseEntity<Map<String, String>> handleHighestPriceFetchException(HighestPriceFetchException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxBotsLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxBotsLimitExceededException(MaxBotsLimitExceededException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BotEnabledException.class)
    public ResponseEntity<Map<String, String>> handleBotEnabledException(BotEnabledException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            if (errors.containsKey(fieldName)) {
                errors.put(fieldName, errors.get(fieldName) + "; " + errorMessage);
            } else {
                errors.put(fieldName, errorMessage);
            }
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SellOrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSellOrderNotFoundException(SellOrderNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of("status", "failure", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

}
