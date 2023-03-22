package com.example.telproject.repository;

import com.example.telproject.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    @Transactional
    @Modifying
    @Query("update Manager m set m.updatedAt = ?1 where m.updatedAt = ?2")
    void updatedManager(Timestamp updatedAt, Timestamp updatedAt1);

    @Query("SELECT m FROM Manager m WHERE m.firstName=?1")
    Optional<Manager> findManagerByFirstName(String firstName);

    @Query("SELECT m FROM Manager m WHERE m.firstName=?1 and m.lastName=?2")
    Optional<Manager> findManagerByFullName(String firstName, String lastName);

    @Modifying
    @Query("UPDATE Manager m SET m.updatedAt=?1 WHERE m.firstName=?2 and m.lastName=?3")
    void updatedManager(Timestamp time, String firstName, String lastName);
}
