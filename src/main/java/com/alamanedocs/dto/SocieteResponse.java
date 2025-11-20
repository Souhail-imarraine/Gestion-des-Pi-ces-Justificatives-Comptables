package com.alamanedocs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocieteResponse {
    
    private Long id;
    private String raisonSociale;
    private String ice;
    private String adresse;
    private String telephone;
    private String emailContact;
    private LocalDateTime dateCreation;
}