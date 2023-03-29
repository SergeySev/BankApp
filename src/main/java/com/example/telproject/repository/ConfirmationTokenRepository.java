package com.example.telproject.repository;

import com.example.telproject.entity.Client;
import com.example.telproject.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    @Transactional
    @Modifying
    void deleteByClient(Client client);

    @Query("SELECT c FROM ConfirmationToken c where c.token=?1")
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmed_at=?1 WHERE c.token=?2")
    void updateConfirmedAt(LocalDateTime confirmedAt, String token);


}
