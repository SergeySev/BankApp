package com.example.telproject.repository;

import com.example.telproject.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    @Query("SELECT m FROM Manager m WHERE m.firstName=?1")
    Optional<Manager> findManagerByFirstName(String firstName);

    @Query("SELECT m FROM Manager m WHERE m.firstName=?1 and m.lastName=?2")
    Optional<Manager> findManagerByFullName(String firstName, String lastName);


}
