package com.example.telproject.repository;

import com.example.telproject.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Modifying
    @Query("update Manager m set m.updated_at = ?1 where m.updated_at = ?2")
    void updatedManager(Timestamp updatedAt, Timestamp updatedAt1);

    @Query("SELECT m FROM Manager m WHERE m.first_name=?1")
    Optional<Manager> findManagerByFirstName(String firstName);

    @Query("SELECT m FROM Manager m WHERE m.first_name=?1 and m.last_name=?2 and m.birth_date=?3")
    Optional<Manager> findManagerByFullNameAndBirthDay(String firstName, String lastName, Timestamp birthDay);



}
