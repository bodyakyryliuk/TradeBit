package com.tradebit.http.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    private String email;
}