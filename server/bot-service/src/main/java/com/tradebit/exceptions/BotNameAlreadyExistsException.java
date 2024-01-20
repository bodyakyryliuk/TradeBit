package com.tradebit.exceptions;

public class BotNameAlreadyExistsException extends RuntimeException{
    public BotNameAlreadyExistsException(){
        super("Provided bot name already exists");
    }
}
