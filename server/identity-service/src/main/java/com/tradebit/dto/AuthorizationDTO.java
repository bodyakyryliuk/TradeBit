package com.tradebit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationDTO {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email;

    @NotBlank(message = "Password is mandatory")
    String password;
}
