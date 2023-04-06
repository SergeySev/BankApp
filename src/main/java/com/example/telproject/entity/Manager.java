package com.example.telproject.entity;

import com.example.telproject.entity.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manager")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    @Enumerated(EnumType.STRING)
    private ManagerStatus status;
    private String email;
    private String phone_number;
    private Timestamp birth_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Client> clients;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Product> products;

    public Manager(String first_name, String lastName, ManagerStatus status, Timestamp birthDate, String email, String phone_number) {
        this.first_name = first_name;
        this.last_name = lastName;
        this.status = status;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
        this.updated_at = Timestamp.valueOf(LocalDateTime.now());
        this.birth_date = birthDate;
        this.email = email;
        this.phone_number = phone_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return id.equals(manager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
