package com.alamanedocs.dto;

import com.alamanedocs.enums.Role;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String email;
    private String nomComplet;
    private Role role;
    private String societeNom;
}