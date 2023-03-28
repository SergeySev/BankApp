package com.example.telproject.entity;

import com.example.telproject.entity.enums.AccountStatus;
import com.example.telproject.entity.enums.AccountType;
import com.example.telproject.entity.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

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
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    private String name;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private CurrencyType currency_code;
    private Timestamp created_at;
    private Timestamp updated_at;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "account")
    @ToString.Exclude
    private Set<Agreement> agreements;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "debitAccount")
    @ToString.Exclude
    private Set<Transaction> debitTransactionList;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "creditAccount")
    @ToString.Exclude
    private Set<Transaction> creditTransactionList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) && client.equals(account.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client);
    }
}
