package com.tradebit.exceptions;

public class BinanceLinkNotFoundException extends RuntimeException{
    public BinanceLinkNotFoundException(String message){
        super(message);
    }
}
