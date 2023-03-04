package com.example.telproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    private Client clientId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false)
    private byte type;

    @Column(name = "status", nullable = false)
    private byte status;

    @Column(name = "balance", nullable = false, precision = 2)
    private BigDecimal balance;

    @Basic
    @Column(name = "currency_code", nullable = false)
    private Integer currencyCode;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Set<Agreement> agreements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return type == account.type && status == account.status && Objects.equals(id, account.id) && Objects.equals(clientId, account.clientId) && Objects.equals(name, account.name) && Objects.equals(balance, account.balance) && Objects.equals(currencyCode, account.currencyCode) && Objects.equals(createdAt, account.createdAt) && Objects.equals(updatedAt, account.updatedAt) && Objects.equals(agreements, account.agreements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, name, type, status, balance, currencyCode, createdAt, updatedAt, agreements);
    }
}
