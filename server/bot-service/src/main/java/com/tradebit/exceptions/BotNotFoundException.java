package com.tradebit.exceptions;

public class BotNotFoundException extends RuntimeException{
    public BotNotFoundException(Long botId){
        super("Bot " + botId + " not found!");
    }
}
