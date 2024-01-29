package com.tradebit.service;

import com.tradebit.dto.RegistrationDTO;
import com.tradebit.user.models.User;


public interface RegistrationService {
    String register(RegistrationDTO user);
    void confirmRegistration(String token);
    void sendVerificationLink(User user);
    void sendVerificationLink(String userId);

}
