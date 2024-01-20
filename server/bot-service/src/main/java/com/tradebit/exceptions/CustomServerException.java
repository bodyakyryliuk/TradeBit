package com.tradebit.exceptions;

public class CustomServerException extends RuntimeException{
    public CustomServerException(String message, int statusCode){
        super(message);
    }
}
