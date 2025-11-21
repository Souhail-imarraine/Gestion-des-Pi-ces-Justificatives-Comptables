package com.alamanedocs.repository;

import com.alamanedocs.entity.Document;
import com.alamanedocs.entity.Societe;
import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.enums.DocumentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    private Societe societe;

    @BeforeEach
    void setUp() {
        societe = Societe.builder()
                .raisonSociale("Test Company")
                .ice("001234567890001")
                .build();
        entityManager.persistAndFlush(societe);
    }

    @Test
    void shouldFindDocumentsBySocieteId() {
        // Given
        Document doc1 = createDocument("DOC-001", DocumentStatus.EN_ATTENTE);
        Document doc2 = createDocument("DOC-002", DocumentStatus.VALIDE);
        entityManager.persistAndFlush(doc1);
        entityManager.persistAndFlush(doc2);

        // When
        List<Document> documents = documentRepository.findBySocieteId(societe.getId());

        // Then
        assertThat(documents).hasSize(2);
        assertThat(documents).extracting(Document::getNumeroPiece)
                .containsExactlyInAnyOrder("DOC-001", "DOC-002");
    }

    @Test
    void shouldFindDocumentsByStatut() {
        // Given
        Document doc1 = createDocument("DOC-001", DocumentStatus.EN_ATTENTE);
        Document doc2 = createDocument("DOC-002", DocumentStatus.EN_ATTENTE);
        Document doc3 = createDocument("DOC-003", DocumentStatus.VALIDE);
        entityManager.persistAndFlush(doc1);
        entityManager.persistAndFlush(doc2);
        entityManager.persistAndFlush(doc3);

        // When
        List<Document> pendingDocs = documentRepository.findByStatut(DocumentStatus.EN_ATTENTE);

        // Then
        assertThat(pendingDocs).hasSize(2);
        assertThat(pendingDocs).allMatch(doc -> doc.getStatut() == DocumentStatus.EN_ATTENTE);
    }

    @Test
    void shouldFindDocumentsBySocieteIdAndStatut() {
        // Given
        Document doc1 = createDocument("DOC-001", DocumentStatus.EN_ATTENTE);
        Document doc2 = createDocument("DOC-002", DocumentStatus.VALIDE);
        entityManager.persistAndFlush(doc1);
        entityManager.persistAndFlush(doc2);

        // When
        List<Document> documents = documentRepository.findBySocieteIdAndStatut(
                societe.getId(), DocumentStatus.EN_ATTENTE);

        // Then
        assertThat(documents).hasSize(1);
        assertThat(documents.get(0).getNumeroPiece()).isEqualTo("DOC-001");
        assertThat(documents.get(0).getStatut()).isEqualTo(DocumentStatus.EN_ATTENTE);
    }

    private Document createDocument(String numeroPiece, DocumentStatus statut) {
        return Document.builder()
                .numeroPiece(numeroPiece)
                .type(DocumentType.FACTURE_ACHAT)
                .datePiece(LocalDate.now())
                .montant(BigDecimal.valueOf(1000))
                .fichierPath("test.pdf")
                .statut(statut)
                .societe(societe)
                .build();
    }
}
