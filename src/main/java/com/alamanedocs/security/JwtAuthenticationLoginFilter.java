package com.alamanedocs.security;

import com.alamanedocs.dto.AuthResponse;
import com.alamanedocs.dto.LoginRequest;
import com.alamanedocs.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
        try {
            if (!request.getMethod().equals("POST")) {
                throw new RuntimeException("Authentication method not supported: " + request.getMethod());
            }
            
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            
            setDetails(request, authToken);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture des credentials", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) 
            throws IOException, ServletException {
        
        User user = (User) authResult.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);
        
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .nomComplet(user.getNomComplet())
                .role(user.getRole().name())
                .build();
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) 
            throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String errorMessage = "{\"error\": \"Unauthorized\", \"message\": \"Email ou mot de passe incorrect\"}";
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
    }
}
