package com.tradebit.exception;

public class UserCreationException extends RuntimeException{
    private int statusCode;
    public UserCreationException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode(){
        return statusCode;
    }
}
