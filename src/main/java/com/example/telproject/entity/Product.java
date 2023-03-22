package com.example.telproject.entity;

import com.example.telproject.entity.enums.CurrencyType;
import com.example.telproject.entity.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @GeneratedValue(generator = "UUID", strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column(name = "name", nullable = false, length = 70)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Column(name = "currency_code", nullable = false)
    private CurrencyType currencyCode;

    @Column(name = "interest_rate", nullable = false, precision = 4)
    private BigDecimal interestRate;

    @Column(name = "rest", nullable = false)
    private Integer rest;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "product")
    @ToString.Exclude
    private Set<Agreement> agreements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
