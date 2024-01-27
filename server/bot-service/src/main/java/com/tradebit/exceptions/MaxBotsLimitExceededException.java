package com.tradebit.exceptions;

public class MaxBotsLimitExceededException extends RuntimeException{
    public MaxBotsLimitExceededException(String message){
        super(message);
    }
}
