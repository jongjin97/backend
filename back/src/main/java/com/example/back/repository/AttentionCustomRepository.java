package com.example.back.repository;

import com.example.back.dto.AttentionRequestDto;
import com.example.back.dto.MainProductDto;
import com.example.back.dto.ProductSearchDto;

import java.util.List;

public interface AttentionCustomRepository {

    List<AttentionRequestDto> findProductAndImgUrl(Long userId);
    List<AttentionRequestDto> findLikeProductOfPdTitle(String searchQuery, Long userId);
}
