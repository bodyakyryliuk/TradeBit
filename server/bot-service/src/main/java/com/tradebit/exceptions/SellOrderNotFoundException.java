package com.tradebit.exceptions;

public class SellOrderNotFoundException extends RuntimeException{
    public SellOrderNotFoundException(String message){
        super(message);
    }
}
