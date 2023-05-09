package com.example.telproject.entity;

import com.example.telproject.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "from_account_id")
    private Account from_account_id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account to_account_id;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private Timestamp created_at;

    public Transaction(Account from_account_id, Account to_account_id, TransactionType type, BigDecimal amount, String description) {
        this.from_account_id = from_account_id;
        this.to_account_id = to_account_id;
        this.type = type;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.description = description;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
