package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.PurchaseDto;
import com.example.back.dto.PurchaseHistoryDto;
import com.example.back.jpa.service.PurchaseHistoryService;
import com.example.back.mybatis.mapper.PurchaseHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchasehistories")
@RequiredArgsConstructor
public class PurchaseHistoryController {
    private final PurchaseHistoryService purchaseHistoryService;
    private final PurchaseHistoryMapper purchaseHistoryMapper;

    // 구매 이력 추가
    @PostMapping("/add")
    public ResponseEntity<String> addPurchaseHistory(@RequestBody PurchaseHistoryDto purchaseHistoryDto, @AuthenticationPrincipal PrincipalDetail principalDetail){

        Long id = purchaseHistoryDto.getUserId(principalDetail);

        //Long userId = purchaseHistoryDto.getUserId();

        purchaseHistoryMapper.createPurchaseHistory(purchaseHistoryDto, id);
        return new ResponseEntity<>("ok",HttpStatus.OK);
    }

    // 구매 이력 목록 조회
    @GetMapping
    public List<Map<String,Object>> purchaseHistories(){
        return purchaseHistoryMapper.purchaseHistories();
    }

    // 구매 이력 상세 조회
    @GetMapping("/{purchaseId}")
    public ResponseEntity<Map<String,Object>> purchaseHistory(@PathVariable Long purchaseId){
        Map<String, Object> result = purchaseHistoryMapper.selectPurchaseHistoryDetail(purchaseId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 구매 이력 수정
//    @PutMapping("/{purchaseId}/edit")
//    public ResponseEntity<?> editPurchaseHistory(@PathVariable Long purchaseId, @RequestBody PurchaseHistoryDto purchaseHistoryDto){
//        purchaseHistoryDto.setId(purchaseId);
//        purchaseHistoryMapper.updatePurchaseHistory(purchaseHistoryDto);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    // 구매 이력 삭제
    @DeleteMapping("/{purchaseHistoryId}")
    public void deletePurchaseHistory(@PathVariable Long purchaseHistoryId){
        purchaseHistoryMapper.deletePurchaseHistory(purchaseHistoryId);
    }

    @GetMapping("/purchase")
    public ResponseEntity<List<PurchaseDto>> findMyPurchaseHistory(@AuthenticationPrincipal PrincipalDetail principalDetail){
        List<PurchaseDto> list = purchaseHistoryService.findPurchaseHistory(principalDetail.getId());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/sale")
    public ResponseEntity<List<PurchaseDto>> findMySaleHistory(@AuthenticationPrincipal PrincipalDetail principalDetail){
        List<PurchaseDto> list = purchaseHistoryService.findSaleHistory(principalDetail.getId());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
