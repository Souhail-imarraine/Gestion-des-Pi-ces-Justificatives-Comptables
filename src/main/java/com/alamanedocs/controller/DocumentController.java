package com.alamanedocs.controller;

import com.alamanedocs.dto.ApiResponse;
import com.alamanedocs.dto.DocumentRequest;
import com.alamanedocs.dto.DocumentResponse;
import com.alamanedocs.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_SOCIETE')")
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @RequestParam("numeroPiece") String numeroPiece,
            @RequestParam("type") String type,
            @RequestParam("datePiece") String datePiece,
            @RequestParam("montant") String montant,
            @RequestParam(value = "categorieComptable", required = false) String categorieComptable,
            @RequestParam(value = "fournisseur", required = false) String fournisseur,
            @RequestParam("file") MultipartFile file) {
        DocumentResponse response = documentService.uploadDocument(
                numeroPiece, type, datePiece, montant, categorieComptable, fournisseur, file);
        return ResponseEntity.ok(ApiResponse.success("Document uploadé avec succès", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SOCIETE')")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getMyDocuments() {
        List<DocumentResponse> documents = documentService.getDocumentsBySociete();
        return ResponseEntity.ok(ApiResponse.success("Documents récupérés", documents));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SOCIETE')")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@PathVariable Long id) {
        DocumentResponse document = documentService.getDocumentById(id);
        return ResponseEntity.ok(ApiResponse.success("Document récupéré", document));
    }
}
