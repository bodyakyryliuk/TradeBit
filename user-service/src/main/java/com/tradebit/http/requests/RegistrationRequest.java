package com.tradebit.http.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    String email;
    String password;
    String firstname;
    String lastname;
}
