package com.atwaha.springsecurity.model.dto;

import com.atwaha.springsecurity.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserDetails {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
