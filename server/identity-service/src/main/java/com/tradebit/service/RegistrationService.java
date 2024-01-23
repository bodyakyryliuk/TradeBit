package com.tradebit.service;

import com.tradebit.requests.RegistrationRequest;


public interface RegistrationService {
    void register(RegistrationRequest user);
    void confirmRegistration(String token);

}
