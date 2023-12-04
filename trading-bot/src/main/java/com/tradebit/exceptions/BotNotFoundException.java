package com.tradebit.exceptions;

public class BotNotFoundException extends RuntimeException{
    public BotNotFoundException(Long botId){
        super("Bot with id: " + botId + " not found");
    }
}
