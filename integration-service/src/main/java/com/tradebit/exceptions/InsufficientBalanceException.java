package com.tradebit.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(){
        super("Account has insufficient balance for requested action.");
    };
}
