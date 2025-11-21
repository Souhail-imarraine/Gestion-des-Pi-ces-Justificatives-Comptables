package com.alamanedocs.service;

import com.alamanedocs.dto.DocumentResponse;
import com.alamanedocs.entity.Document;
import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final DocumentRepository documentRepository;

    public List<DocumentResponse> getPendingDocuments() {
        return documentRepository.findByStatut(DocumentStatus.EN_ATTENTE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse validerDocument(Long id, String commentaire) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        document.setStatut(DocumentStatus.VALIDE);
        document.setDateValidation(LocalDateTime.now());
        document.setCommentaireComptable(commentaire);
        
        document = documentRepository.save(document);
        return mapToResponse(document);
    }

    public DocumentResponse rejeterDocument(Long id, String motif) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        document.setStatut(DocumentStatus.REJETE);
        document.setDateValidation(LocalDateTime.now());
        document.setMotifRejet(motif);
        
        document = documentRepository.save(document);
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
