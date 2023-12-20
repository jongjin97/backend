package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.SelectProductDto;
import com.example.back.jpa.service.SelectProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/selectProduct")
public class SelectProductController {

    private final SelectProductService selectProductService;

    @GetMapping
    public List<SelectProductDto> findAllByUserId(@AuthenticationPrincipal PrincipalDetail principalDetail
            , @RequestParam(value = "period", required = false) String period){
        return selectProductService.getSelectProduct(principalDetail.getId(), period);
    }
}
