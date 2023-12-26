package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
//import com.example.back.service.AttentionService;
import com.example.back.dto.AttentionDto;
import com.example.back.jpa.service.AttentionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/attention")
public class AttentionController {

    private final AttentionService attentionService;

    //등록한 관심 상품 조회
    @GetMapping("/lists")
    public ResponseEntity<List<AttentionDto>> getAttentionListByUserId(@AuthenticationPrincipal PrincipalDetail principalDetail){

        List<AttentionDto> attentionDtoList = attentionService.selectAttentionListByUserId(principalDetail.getId());

        return ResponseEntity.ok(attentionDtoList);
    }

    //관심 상품 등록
    @PostMapping
    public ResponseEntity<AttentionDto> saveAndUpdateAttention(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody AttentionDto attentionDto){
        AttentionDto attentionResult = attentionService.saveAndUpdateAttention(principalDetail, attentionDto);

        return ResponseEntity.ok(attentionResult);
    }


}
