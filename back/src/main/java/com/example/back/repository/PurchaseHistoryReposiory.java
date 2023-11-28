package com.example.back.repository;

import com.example.back.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHistoryReposiory extends JpaRepository<PurchaseHistory, Long>, PurchaseHistoryCustomRepository {
}
