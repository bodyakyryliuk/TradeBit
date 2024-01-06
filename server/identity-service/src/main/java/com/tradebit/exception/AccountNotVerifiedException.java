package com.tradebit.exception;

public class AccountNotVerifiedException extends RuntimeException{
    public AccountNotVerifiedException(){
        super("Please verify your account.");
    }
}
