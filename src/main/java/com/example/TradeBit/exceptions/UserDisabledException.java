package com.example.TradeBit.exceptions;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException(String message){
        super(message);
    }
}
