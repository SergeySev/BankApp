package com.example.telproject.repository;

import com.example.telproject.entity.ClientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientDocumentRepository extends JpaRepository<ClientDocument, UUID> {
    Optional<ClientDocument> findByClientId(Long clientId);
}
