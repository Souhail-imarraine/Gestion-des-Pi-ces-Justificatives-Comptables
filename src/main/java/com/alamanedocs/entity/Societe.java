package com.alamanedocs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "societes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Societe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String raisonSociale;
    
    @Column(unique = true, nullable = false, length = 15)
    private String ice;
    
    private String adresse;
    
    private String telephone;
    
    private String emailContact;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
