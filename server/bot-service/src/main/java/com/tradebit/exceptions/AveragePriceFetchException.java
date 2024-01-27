package com.tradebit.exceptions;

public class AveragePriceFetchException extends RuntimeException{
    public AveragePriceFetchException(String tradingPair, int period){
        super("Failed to fetch average price for trading pair: " + tradingPair + " , period: " + period);
    }
}
