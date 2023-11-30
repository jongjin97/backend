package com.example.back.jpa.service;

import com.example.back.dto.PurchaseDto;
import com.example.back.repository.PurchaseHistoryReposiory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryReposiory purchaseHistoryReposiory;

    public List<PurchaseDto> findPurchaseHistory(Long id, String title, String period){
        return purchaseHistoryReposiory.findPurchaseHistory(id, title, period);
    }

    public List<PurchaseDto> findSaleHistory(Long id, String title, String period){
        return purchaseHistoryReposiory.findSaleHistory(id, title, period);
    }
}
