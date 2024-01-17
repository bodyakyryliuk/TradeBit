package com.tradebit.exceptions;

public class HighestPriceFetchException extends RuntimeException{
    public HighestPriceFetchException(String tradingPair, int period){
        super("Failed to fetch highest price for trading pair: " + tradingPair + " , period: " + period);
    }
}
