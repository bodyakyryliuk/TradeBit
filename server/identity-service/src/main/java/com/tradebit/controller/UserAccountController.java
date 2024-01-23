package com.tradebit.controller;

import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAccountController {
    private final RegistrationService registrationService;

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token){
        try {
            registrationService.confirmRegistration(token);
            return "success-email-confirm";
        } catch (InvalidTokenException | UserNotFoundException e) {
            return "failed-email-confirm";
        }
    }


}
