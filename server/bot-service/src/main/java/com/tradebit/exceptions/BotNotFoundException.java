package com.tradebit.exceptions;

public class BotNotFoundException extends RuntimeException{
    public BotNotFoundException(String message){
        super(message);
    }
}
