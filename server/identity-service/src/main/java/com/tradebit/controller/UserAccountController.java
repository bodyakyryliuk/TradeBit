package com.tradebit.controller;

import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.resetToken.ResetTokenService;
import com.tradebit.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAccountController {
    private final RegistrationService registrationService;
    private final ResetTokenService resetTokenService;

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token){
        try {
            registrationService.confirmRegistration(token);
            return "success-email-confirm";
        } catch (InvalidTokenException | UserNotFoundException e) {
            return "failed-email-confirm";
        }
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token){
        boolean isValidToken = resetTokenService.isTokenValid(token);
        if (isValidToken) {
            return "password-reset";
        } else {
            return "invalid-token";
        }
    }
}
