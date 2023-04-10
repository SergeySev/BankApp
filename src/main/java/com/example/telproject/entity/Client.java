package com.example.telproject.entity;

import com.example.telproject.entity.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client")
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @ToString.Exclude
    private Manager manager;
    @Enumerated(EnumType.STRING)
    private ClientStatus status;
    private String taxCode;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private Timestamp birth_date;
    private Timestamp created_at;
    private Timestamp updated_at;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "client")
    @ToString.Exclude
    private Set<Account> accountList;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<ConfirmationToken> tokens;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<ClientDocument> clientDocuments;


    public Client(
            Manager manager,
            String first_name,
            String last_name,
            String email,
            Timestamp birth_date,
            String password
    ) {
        this.manager = manager;
        this.status = ClientStatus.PENDING;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
        this.birth_date = birth_date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) && email.equals(client.email) && phone.equals(client.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(status.getValue());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return first_name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != ClientStatus.REMOVED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != ClientStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return status == ClientStatus.ACTIVE;
    }
}
