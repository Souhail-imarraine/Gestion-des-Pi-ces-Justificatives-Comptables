package com.alamanedocs.dto;

import com.alamanedocs.enums.TypeDocument;
import com.alamanedocs.enums.StatutDocument;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    
    private Long id;
    private String numeroPiece;
    private TypeDocument type;
    private String categorieComptable;
    private LocalDate datePiece;
    private BigDecimal montant;
    private String fournisseur;
    private StatutDocument statut;
    private LocalDateTime dateValidation;
    private String commentaireComptable;
    private String societeNom;
    private LocalDateTime dateCreation;
}