package com.example.TradeBit.exceptions;

public class UserExistsException extends RuntimeException{
    public UserExistsException(){
        super("User already exists");
    }
}
