package com.alamanedocs.security;

import com.alamanedocs.entity.User;
import com.alamanedocs.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private User user;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", 
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 86400000L);

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .nomComplet("Test User")
                .role(RoleType.SOCIETE)
                .actif(true)
                .build();
    }

    @Test
    void shouldGenerateToken() {
        // When
        String token = jwtTokenProvider.generateToken(user);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldExtractEmailFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(user);

        // When
        String email = jwtTokenProvider.extractEmail(token);

        // Then
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    void shouldValidateToken() {
        // Given
        String token = jwtTokenProvider.generateToken(user);

        // When
        Boolean isValid = jwtTokenProvider.isTokenValid(token, user);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        try {
            Boolean isValid = jwtTokenProvider.isTokenValid(invalidToken, user);
            assertThat(isValid).isFalse();
        } catch (Exception e) {
            // Expected: invalid token should throw exception
            assertThat(e).isNotNull();
        }
    }

    @Test
    void shouldExtractExpiration() {
        // Given
        String token = jwtTokenProvider.generateToken(user);

        // When
        var expiration = jwtTokenProvider.extractExpiration(token);

        // Then
        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new java.util.Date());
    }
}
