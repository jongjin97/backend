package com.example.back.repository;

import com.example.back.dto.PurchaseDto;

import java.util.List;

public interface PurchaseHistoryCustomRepository {
    List<PurchaseDto> findSaleHistory(Long id, String title, String period);
    List<PurchaseDto> findPurchaseHistory(Long id, String title, String period);

}
