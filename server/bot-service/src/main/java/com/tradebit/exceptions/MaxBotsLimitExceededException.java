package com.tradebit.exceptions;

public class MaxBotsLimitExceededException extends RuntimeException{
    public MaxBotsLimitExceededException(){
        super("Exceeded maximum number of bots allowed");
    }
}
