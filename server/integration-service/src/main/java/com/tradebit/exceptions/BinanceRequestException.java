package com.tradebit.exceptions;

public class BinanceRequestException extends RuntimeException{
    public BinanceRequestException(String message){
        super(message);
    }
}
