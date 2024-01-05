package com.example.back.repository;

import com.example.back.dto.AttentionRequestDto;
import com.example.back.dto.MainProductDto;

import java.util.List;

public interface AttentionCustomRepository {

    List<AttentionRequestDto> findProductAndImgUrl(Long userId);
}
