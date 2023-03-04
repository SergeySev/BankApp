package com.example.telproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "agreement")
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account accountId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product productId;

    @Basic
    @Column(name = "interest_rate", nullable = false, precision = 4)
    private BigDecimal interestRate;

    @Basic
    @Column(name = "status", nullable = false)
    private byte status;

    @Basic
    @Column(name = "sum", nullable = false, precision = 2)
    private BigDecimal sum;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agreement agreement = (Agreement) o;
        return status == agreement.status && id.equals(agreement.id) && accountId.equals(agreement.accountId) && productId.equals(agreement.productId) && interestRate.equals(agreement.interestRate) && sum.equals(agreement.sum) && createdAt.equals(agreement.createdAt) && updatedAt.equals(agreement.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, productId, interestRate, status, sum, createdAt, updatedAt);
    }
}
