package com.example.telproject.entity;

import com.example.telproject.entity.enums.CurrencyType;
import com.example.telproject.entity.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @ToString.Exclude
    private Manager manager;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    @Enumerated(EnumType.STRING)
    private CurrencyType currency_code;
    private BigDecimal interest_rate;
    private Integer rest;
    private Timestamp created_at;
    private Timestamp updated_at;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "product")
    @ToString.Exclude
    private Set<Agreement> agreements;

    public Product(Manager manager, String name, ProductStatus status, CurrencyType currency_code, Integer rest) {
        this.manager = manager;
        this.name = name;
        this.status = status;
        this.currency_code = currency_code;
        this.rest = rest;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(created_at, product.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created_at);
    }
}
