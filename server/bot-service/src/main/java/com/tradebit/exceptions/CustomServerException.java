package com.tradebit.exceptions;

public class CustomServerException extends RuntimeException{
    private final int statusCode;
    public CustomServerException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode(){
        return statusCode;
    }
}
