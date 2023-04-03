package com.example.telproject.repository;

import com.example.telproject.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @Query("SELECT m FROM Manager m WHERE m.first_name=?1")
    List<Manager> findManagerByFirstName(String firstName);

    @Query("SELECT m FROM Manager m WHERE m.first_name=?1 and m.last_name=?2 and m.email=?3")
    Optional<Manager> findManagerByFullNameAndEmail(String firstName, String lastName, String email);

    @Query("SELECT m FROM Manager m WHERE m.email=?1")
    Optional<Manager> findManagerByEmail(String email);

}
