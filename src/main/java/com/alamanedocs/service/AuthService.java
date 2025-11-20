package com.alamanedocs.service;

import com.alamanedocs.dto.AuthResponse;
import com.alamanedocs.dto.LoginRequest;
import com.alamanedocs.entity.Utilisateur;
import com.alamanedocs.repository.UtilisateurRepository;
import com.alamanedocs.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur user = (Utilisateur) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
            token,
            user.getEmail(),
            user.getNomComplet(),
            user.getRole(),
            user.getSociete() != null ? user.getSociete().getRaisonSociale() : null
        );
    }
}