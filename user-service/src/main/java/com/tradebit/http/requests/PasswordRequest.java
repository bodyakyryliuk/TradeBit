package com.tradebit.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequest {
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must have at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must have at least one digit")
    private String password;
}
