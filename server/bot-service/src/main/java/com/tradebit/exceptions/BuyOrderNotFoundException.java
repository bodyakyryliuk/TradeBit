package com.tradebit.exceptions;

public class BuyOrderNotFoundException extends RuntimeException{
    public BuyOrderNotFoundException(Long botId){
        super("Buy order not found by bot with id: " + botId);
    }
}
