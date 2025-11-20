package com.alamanedocs.dto;

import com.alamanedocs.enums.TypeDocument;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DocumentRequest {
    
    @NotBlank
    private String numeroPiece;
    
    @NotNull
    private TypeDocument type;
    
    private String categorieComptable;
    
    @NotNull
    private LocalDate datePiece;
    
    private BigDecimal montant;
    private String fournisseur;
}