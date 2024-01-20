package com.tradebit.exceptions;

public class CustomClientException extends RuntimeException{
    public CustomClientException(String message, int statusCode){
        super(message);
    }
}
