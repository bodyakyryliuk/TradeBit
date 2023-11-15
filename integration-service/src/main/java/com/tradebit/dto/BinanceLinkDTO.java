package com.tradebit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BinanceLinkDTO {
    @NotBlank(message = "API key is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{64}$", message = "Incorrect format of API key")
    String apiKey;

    @NotBlank(message = "Secret API key is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{64}$", message = "Incorrect format of API secret key")
    String secretApiKey;
}
