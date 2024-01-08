package com.tradebit.exceptions;

public class InvalidTopUpCoinException extends RuntimeException{
    public InvalidTopUpCoinException(String message){
        super(message);
    }
}
