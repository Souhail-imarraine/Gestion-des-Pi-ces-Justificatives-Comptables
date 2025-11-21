package com.alamanedocs.entity;

import com.alamanedocs.enums.DocumentStatus;
import com.alamanedocs.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String numeroPiece;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;
    
    private String categorieComptable;
    
    @Column(nullable = false)
    private LocalDate datePiece;
    
    private BigDecimal montant;
    
    private String fournisseur;
    
    @Column(nullable = false)
    private String fichierPath;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus statut = DocumentStatus.EN_ATTENTE;
    
    private LocalDateTime dateValidation;
    
    private String commentaireComptable;
    
    private String motifRejet;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe_id", nullable = false)
    private Societe societe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
