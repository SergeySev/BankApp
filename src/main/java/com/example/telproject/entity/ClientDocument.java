package com.example.telproject.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
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
    @ManyToOne
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;
    private String link;
    private boolean confirm;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Optional<String> getLink() {
        return Optional.ofNullable(link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDocument that = (ClientDocument) o;
        return id.equals(that.id) && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}
