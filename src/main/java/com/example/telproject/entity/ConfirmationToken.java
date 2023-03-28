package com.example.telproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "confirmation_token")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private LocalDateTime created_at;
    private LocalDateTime expired_at;
    private LocalDateTime confirmed_at;

    @ManyToOne
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;

    public ConfirmationToken(Client client) {
        this.token = UUID.randomUUID().toString();
        this.created_at = LocalDateTime.now();
        this.expired_at = LocalDateTime.now().plusMinutes(15);
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationToken that = (ConfirmationToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }
}
