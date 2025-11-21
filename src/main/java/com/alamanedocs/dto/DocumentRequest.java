package com.alamanedocs.dto;

import com.alamanedocs.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {
    
    @NotBlank(message = "Le numéro de pièce est obligatoire")
    private String numeroPiece;
    
    @NotNull(message = "Le type de document est obligatoire")
    private DocumentType type;
    
    private String categorieComptable;
    
    @NotNull(message = "La date de la pièce est obligatoire")
    private LocalDate datePiece;
    
    private BigDecimal montant;
    
    private String fournisseur;
}
