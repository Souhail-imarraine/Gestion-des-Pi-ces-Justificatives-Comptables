package com.alamanedocs.service;

import com.alamanedocs.dto.DocumentResponse;
import com.alamanedocs.entity.Document;
import com.alamanedocs.entity.Societe;
import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.enums.DocumentType;
import com.alamanedocs.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private ValidationService validationService;

    private Document document;
    private Societe societe;

    @BeforeEach
    void setUp() {
        societe = Societe.builder()
                .id(1L)
                .raisonSociale("Test Company")
                .build();

        document = Document.builder()
                .id(1L)
                .numeroPiece("DOC-001")
                .type(DocumentType.FACTURE_ACHAT)
                .datePiece(LocalDate.now())
                .montant(BigDecimal.valueOf(1000))
                .fichierPath("test.pdf")
                .statut(DocumentStatus.EN_ATTENTE)
                .societe(societe)
                .build();
    }

    @Test
    void shouldGetPendingDocuments() {
        // Given
        when(documentRepository.findByStatut(DocumentStatus.EN_ATTENTE))
                .thenReturn(Arrays.asList(document));

        // When
        List<DocumentResponse> documents = validationService.getPendingDocuments();

        // Then
        assertThat(documents).hasSize(1);
        assertThat(documents.get(0).getStatut()).isEqualTo(DocumentStatus.EN_ATTENTE);
        verify(documentRepository).findByStatut(DocumentStatus.EN_ATTENTE);
    }

    @Test
    void shouldValiderDocument() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        // When
        DocumentResponse response = validationService.validerDocument(1L, "Document conforme");

        // Then
        assertThat(response).isNotNull();
        assertThat(document.getStatut()).isEqualTo(DocumentStatus.VALIDE);
        assertThat(document.getCommentaireComptable()).isEqualTo("Document conforme");
        assertThat(document.getDateValidation()).isNotNull();
        verify(documentRepository).save(document);
    }

    @Test
    void shouldRejeterDocument() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        // When
        DocumentResponse response = validationService.rejeterDocument(1L, "Document incomplet");

        // Then
        assertThat(response).isNotNull();
        assertThat(document.getStatut()).isEqualTo(DocumentStatus.REJETE);
        assertThat(document.getMotifRejet()).isEqualTo("Document incomplet");
        assertThat(document.getDateValidation()).isNotNull();
        verify(documentRepository).save(document);
    }

    @Test
    void shouldThrowExceptionWhenDocumentNotFoundForValidation() {
        // Given
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> validationService.validerDocument(999L, "Test"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Document non trouvé");
    }
}
