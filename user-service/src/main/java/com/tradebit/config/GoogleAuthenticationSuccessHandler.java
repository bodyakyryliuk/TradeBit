package com.tradebit.config;

import com.tradebit.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoogleAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RegistrationService registrationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Handle authentication success
        System.out.println("In onAuthenticationSuccess");
        registrationService.processGoogleLogin(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }


}
