package com.example.telproject.entity;

import com.example.telproject.entity.enums.AccountProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agreement")
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private BigDecimal interest_rate;
    @Enumerated(EnumType.STRING)
    private AccountProductStatus status;
    private BigDecimal sum;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Agreement(Account account, Product product, AccountProductStatus status, BigDecimal sum) {
        this.account = account;
        this.product = product;
        this.status = status;
        this.sum = sum;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agreement agreement = (Agreement) o;
        return id.equals(agreement.id) && account.equals(agreement.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account);
    }
}
