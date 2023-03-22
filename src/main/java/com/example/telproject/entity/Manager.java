package com.example.telproject.entity;

import com.example.telproject.entity.enums.ManagerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manager")
public class Manager {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ManagerStatus status;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Client> clients;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "manager", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Product> products;

    public Manager(String firstName, String lastName, ManagerStatus status) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());

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
