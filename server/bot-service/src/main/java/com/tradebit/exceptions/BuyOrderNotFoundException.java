package com.tradebit.exceptions;

public class BuyOrderNotFoundException extends RuntimeException{
    public BuyOrderNotFoundException(String message){
        super(message);
    }
}
