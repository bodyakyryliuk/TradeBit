package com.tradebit.http.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email;

    @NotBlank(message = "Password is mandatory")
    String password;
}
