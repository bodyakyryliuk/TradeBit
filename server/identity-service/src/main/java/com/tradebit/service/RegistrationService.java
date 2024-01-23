package com.tradebit.service;

import com.tradebit.requests.RegistrationRequest;
import com.tradebit.user.models.User;


public interface RegistrationService {
    void register(RegistrationRequest user);
    void confirmRegistration(String token);
    void sendVerificationLink(User user);
    void sendVerificationLink(String userId);

}
