package com.example.telproject.repository;

import com.example.telproject.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a JOIN a.agreements ag WHERE ag.product.id = ?1")
    List<Account> findByProductId(Long productId);

    @Query("SELECT a FROM Account a WHERE a.status='ACTIVE'")
    List<Account> findAllActive();
}
