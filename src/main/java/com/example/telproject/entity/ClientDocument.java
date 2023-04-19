package com.example.telproject.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
@Table(name = "client_document")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class ClientDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;
    private byte[] document;
    private String type;
    private boolean confirm;
    private Timestamp created_at;
    private Timestamp updated_at;

    public ClientDocument(Client client, byte[] document, String type) {
        this.client = client;
        this.document = document;
        this.type = type;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDocument that = (ClientDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
