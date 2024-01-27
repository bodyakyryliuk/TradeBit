package com.tradebit.exceptions;

public class CurrentPriceFetchException extends RuntimeException{
    public CurrentPriceFetchException(String tradingPair){
        super("Failed to fetch current price for trading pair: " + tradingPair );
    }
}
