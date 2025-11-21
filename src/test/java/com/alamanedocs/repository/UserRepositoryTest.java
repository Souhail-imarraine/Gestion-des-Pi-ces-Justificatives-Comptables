package com.alamanedocs.repository;

import com.alamanedocs.entity.User;
import com.alamanedocs.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nomComplet("Test User")
                .role(RoleType.SOCIETE)
                .actif(true)
                .build();
        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getRole()).isEqualTo(RoleType.SOCIETE);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckIfEmailExists() {
        // Given
        User user = User.builder()
                .email("existing@example.com")
                .password("password123")
                .nomComplet("Existing User")
                .role(RoleType.COMPTABLE)
                .actif(true)
                .build();
        entityManager.persistAndFlush(user);

        // When
        boolean exists = userRepository.existsByEmail("existing@example.com");
        boolean notExists = userRepository.existsByEmail("notexisting@example.com");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
