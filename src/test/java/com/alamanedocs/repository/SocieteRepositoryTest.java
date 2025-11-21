package com.alamanedocs.repository;

import com.alamanedocs.entity.Societe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SocieteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SocieteRepository societeRepository;

    @Test
    void shouldFindSocieteByIce() {
        // Given
        Societe societe = Societe.builder()
                .raisonSociale("Test Company")
                .ice("001234567890001")
                .adresse("123 Test Street")
                .telephone("0612345678")
                .emailContact("contact@test.com")
                .build();
        entityManager.persistAndFlush(societe);

        // When
        Optional<Societe> found = societeRepository.findByIce("001234567890001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getRaisonSociale()).isEqualTo("Test Company");
        assertThat(found.get().getIce()).isEqualTo("001234567890001");
    }

    @Test
    void shouldReturnEmptyWhenIceNotFound() {
        // When
        Optional<Societe> found = societeRepository.findByIce("999999999999999");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckIfIceExists() {
        // Given
        Societe societe = Societe.builder()
                .raisonSociale("Existing Company")
                .ice("001234567890002")
                .build();
        entityManager.persistAndFlush(societe);

        // When
        boolean exists = societeRepository.existsByIce("001234567890002");
        boolean notExists = societeRepository.existsByIce("999999999999999");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
