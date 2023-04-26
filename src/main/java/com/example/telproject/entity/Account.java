package com.example.telproject.entity;

import com.example.telproject.entity.enums.AccountStatus;
import com.example.telproject.entity.enums.AccountType;
import com.example.telproject.entity.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.types.enums.CreditCardType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
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
    private String card_number;
    private String csv;
    private Timestamp expired_at;
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

    public Account(Client client, CurrencyType currency_code) {
        this.client = client;
        this.card_number = MockNeat.threadLocal().creditCards().type(CreditCardType.VISA_16).get();
        this.csv = generateCsv();
        this.expired_at = Timestamp.valueOf(LocalDateTime.now().plusYears(5));
        this.currency_code = currency_code;
        this.balance = BigDecimal.ZERO;
    }

    private String generateCsv() {
        Random random = new Random();
        StringBuilder csv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            csv.append(random.nextInt(0, 10));
        }
        return csv.toString();
    }

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
