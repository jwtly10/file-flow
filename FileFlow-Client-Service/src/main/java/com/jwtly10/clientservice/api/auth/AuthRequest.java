package com.jwtly10.clientservice.api.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String firstname;
    private String username;
    private String lastname;
    private String email;
    private String password;
}
