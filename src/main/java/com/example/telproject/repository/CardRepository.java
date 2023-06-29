package com.example.telproject.repository;

import com.example.telproject.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT a FROM Card a JOIN a.agreements ag WHERE ag.product.id = ?1")
    List<Card> findByProductId(Long productId);

    @Query("SELECT a FROM Card a WHERE a.status='ACTIVE'")
    List<Card> findAllActive();
}
