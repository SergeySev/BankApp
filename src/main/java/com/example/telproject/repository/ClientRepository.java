package com.example.telproject.repository;

import com.example.telproject.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.first_name=?1 and c.last_name=?2")
    List<Client> findClientByName(String first_name, String last_name);

    @Transactional
    @Modifying
    @Query("UPDATE Client  c SET c.status=?1 WHERE c.email=?2")
    void activeClient(String status, String email);


}
