package com.tradebit.exceptions;

public class InvalidSymbolException extends RuntimeException{
    public InvalidSymbolException(String message){
        super(message);
    }
}
