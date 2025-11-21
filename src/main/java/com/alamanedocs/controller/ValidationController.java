package com.alamanedocs.controller;

import com.alamanedocs.dto.ApiResponse;
import com.alamanedocs.dto.DocumentResponse;
import com.alamanedocs.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/validations")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_COMPTABLE')")
public class ValidationController {

    private final ValidationService validationService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getPendingDocuments() {
        List<DocumentResponse> documents = validationService.getPendingDocuments();
        return ResponseEntity.ok(ApiResponse.success("Documents en attente récupérés", documents));
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<ApiResponse<DocumentResponse>> validerDocument(
            @PathVariable Long id,
            @RequestParam(required = false) String commentaire) {
        DocumentResponse document = validationService.validerDocument(id, commentaire);
        return ResponseEntity.ok(ApiResponse.success("Document validé", document));
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<ApiResponse<DocumentResponse>> rejeterDocument(
            @PathVariable Long id,
            @RequestParam String motif) {
        DocumentResponse document = validationService.rejeterDocument(id, motif);
        return ResponseEntity.ok(ApiResponse.success("Document rejeté", document));
    }
}
