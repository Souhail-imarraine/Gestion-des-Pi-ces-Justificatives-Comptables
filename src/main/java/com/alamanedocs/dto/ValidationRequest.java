package com.alamanedocs.dto;

import com.alamanedocs.enums.StatutDocument;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ValidationRequest {
    
    @NotNull
    private StatutDocument statut;
    
    private String commentaireComptable;
}