package com.tradebit.exceptions;

public class FailedToParseException extends RuntimeException{
    public FailedToParseException(String message){
        super(message);
    }
}
