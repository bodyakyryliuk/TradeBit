package com.tradebit.exceptions;

public class BotEnabledException extends RuntimeException{
    public BotEnabledException(String message){
        super(message);
    }
}
