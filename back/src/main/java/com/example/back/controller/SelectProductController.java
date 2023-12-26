package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.SelectProductDto;
import com.example.back.jpa.service.SelectProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/selectProduct")
public class SelectProductController {

    private final SelectProductService selectProductService;

    @GetMapping
    public ResponseEntity<List<SelectProductDto>> findAllByUserId(@AuthenticationPrincipal PrincipalDetail principalDetail
            , @RequestParam(value = "period", required = false) String period){
        List<SelectProductDto> list = selectProductService.getSelectProduct(principalDetail.getId(), period);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSelectProduct(@PathVariable Long id){
        selectProductService.deleteSelectProduct(id);
        return ResponseEntity.ok().build();
    }
}
