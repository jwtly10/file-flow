package com.jwtly10.databaseservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    // This is a requirement (for now) of the Supabase API
    private String user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
}
