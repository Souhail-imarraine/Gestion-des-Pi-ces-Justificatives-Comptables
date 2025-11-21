package com.alamanedocs.service;

import com.alamanedocs.dto.DocumentRequest;
import com.alamanedocs.dto.DocumentResponse;
import com.alamanedocs.entity.Document;
import com.alamanedocs.entity.User;
import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public DocumentResponse uploadDocument(
            String numeroPiece, String type, String datePiece, String montant,
            String categorieComptable, String fournisseur, MultipartFile file) {
        
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String filePath = fileStorageService.saveFile(file);
        
        Document document = Document.builder()
                .numeroPiece(numeroPiece)
                .type(com.alamanedocs.enums.DocumentType.valueOf(type))
                .categorieComptable(categorieComptable)
                .datePiece(java.time.LocalDate.parse(datePiece))
                .montant(new java.math.BigDecimal(montant))
                .fournisseur(fournisseur)
                .fichierPath(filePath)
                .statut(DocumentStatus.EN_ATTENTE)
                .societe(currentUser.getSociete())
                .createdBy(currentUser)
                .build();
        
        document = documentRepository.save(document);
        return mapToResponse(document);
    }

    public List<DocumentResponse> getDocumentsBySociete() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return documentRepository.findBySocieteId(currentUser.getSociete().getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        return mapToResponse(document);
    }

    private DocumentResponse mapToResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .numeroPiece(document.getNumeroPiece())
                .type(document.getType())
                .categorieComptable(document.getCategorieComptable())
                .datePiece(document.getDatePiece())
                .montant(document.getMontant())
                .fournisseur(document.getFournisseur())
                .fichierPath(document.getFichierPath())
                .statut(document.getStatut())
                .dateValidation(document.getDateValidation())
                .commentaireComptable(document.getCommentaireComptable())
                .motifRejet(document.getMotifRejet())
                .societeNom(document.getSociete().getRaisonSociale())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
