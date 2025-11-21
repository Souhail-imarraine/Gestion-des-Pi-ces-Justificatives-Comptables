package com.alamanedocs.repository;

import com.alamanedocs.entity.Document;
import com.alamanedocs.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findBySocieteId(Long societeId);
    List<Document> findByStatut(DocumentStatus statut);
    List<Document> findBySocieteIdAndStatut(Long societeId, DocumentStatus statut);
}
