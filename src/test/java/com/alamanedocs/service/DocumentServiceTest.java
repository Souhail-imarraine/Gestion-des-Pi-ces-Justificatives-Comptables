package com.alamanedocs.service;

import com.alamanedocs.dto.DocumentResponse;
import com.alamanedocs.entity.Document;
import com.alamanedocs.entity.Societe;
import com.alamanedocs.entity.User;
import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.enums.DocumentType;
import com.alamanedocs.enums.RoleType;
import com.alamanedocs.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

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
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private DocumentService documentService;

    private User user;
    private Societe societe;
    private Document document;

    @BeforeEach
    void setUp() {
        societe = Societe.builder()
                .id(1L)
                .raisonSociale("Test Company")
                .ice("001234567890001")
                .build();

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .nomComplet("Test User")
                .role(RoleType.SOCIETE)
                .societe(societe)
                .actif(true)
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
                .createdBy(user)
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldUploadDocument() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(fileStorageService.saveFile(any())).thenReturn("saved-file.pdf");
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        // When
        DocumentResponse response = documentService.uploadDocument(
                "DOC-001", "FACTURE_ACHAT", "2024-11-20", "1000.00",
                "Achats", "Test Supplier", multipartFile);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getNumeroPiece()).isEqualTo("DOC-001");
        assertThat(response.getStatut()).isEqualTo(DocumentStatus.EN_ATTENTE);
        verify(fileStorageService).saveFile(multipartFile);
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void shouldGetDocumentsBySociete() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(documentRepository.findBySocieteId(1L)).thenReturn(Arrays.asList(document));

        // When
        List<DocumentResponse> documents = documentService.getDocumentsBySociete();

        // Then
        assertThat(documents).hasSize(1);
        assertThat(documents.get(0).getNumeroPiece()).isEqualTo("DOC-001");
        verify(documentRepository).findBySocieteId(1L);
    }

    @Test
    void shouldGetDocumentById() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        // When
        DocumentResponse response = documentService.getDocumentById(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNumeroPiece()).isEqualTo("DOC-001");
    }

    @Test
    void shouldThrowExceptionWhenDocumentNotFound() {
        // Given
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> documentService.getDocumentById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Document non trouvé");
    }
}
