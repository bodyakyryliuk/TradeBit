package com.tradebit.exception;

public class VerificationTokenNotFoundException extends RuntimeException{
    public VerificationTokenNotFoundException(String message){
        super(message);
    }
}
