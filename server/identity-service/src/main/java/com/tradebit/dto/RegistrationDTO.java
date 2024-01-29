package com.tradebit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must have at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must have at least one digit")
    String password;

    @NotBlank(message = "First name is mandatory")
    @Pattern(regexp = "^[a-zA-Z'\\-\\s]+$", message = "First name contains invalid characters")
    String firstname;

    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^[a-zA-Z'\\-\\s]+$", message = "Last name contains invalid characters")
    String lastname;
}
