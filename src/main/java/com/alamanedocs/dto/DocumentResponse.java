package com.alamanedocs.dto;

import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {
    private Long id;
    private String numeroPiece;
    private DocumentType type;
    private String categorieComptable;
    private LocalDate datePiece;
    private BigDecimal montant;
    private String fournisseur;
    private String fichierPath;
    private DocumentStatus statut;
    private LocalDateTime dateValidation;
    private String commentaireComptable;
    private String motifRejet;
    private String societeNom;
    private LocalDateTime createdAt;
}
