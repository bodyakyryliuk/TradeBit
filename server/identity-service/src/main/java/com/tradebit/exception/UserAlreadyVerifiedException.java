package com.tradebit.exception;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String userId){
        super("User with id: " + userId + " has already verified his email");
    }
}
