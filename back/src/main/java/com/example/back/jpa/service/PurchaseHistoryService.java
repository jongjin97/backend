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

    public List<PurchaseDto> findPurchaseHistory(Long id){
        return purchaseHistoryReposiory.findPurchaseHistory(id);
    }

    public List<PurchaseDto> findSaleHistory(Long id){
        return purchaseHistoryReposiory.findSaleHistory(id);
    }
}
