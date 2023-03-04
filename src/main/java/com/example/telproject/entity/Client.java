package com.example.telproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "client")
public class Client {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Basic
    @Column(name = "status", nullable = false)
    private byte status;

    @Basic
    @Column(name = "tax_code", nullable = false, length = 20)
    private String taxCode;

    @Basic
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Basic
    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Basic
    @Column(name = "address", nullable = false, length = 80)
    private String address;

    @Basic
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Set<Account> accountList = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return status == client.status && id.equals(client.id) && manager.equals(client.manager) && taxCode.equals(client.taxCode) && firstName.equals(client.firstName) && lastName.equals(client.lastName) && email.equals(client.email) && address.equals(client.address) && phone.equals(client.phone) && createdAt.equals(client.createdAt) && updatedAt.equals(client.updatedAt) && accountList.equals(client.accountList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manager, status, taxCode, firstName, lastName, email, address, phone, createdAt, updatedAt, accountList);
    }
}
