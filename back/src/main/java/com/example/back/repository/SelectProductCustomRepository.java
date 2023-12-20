package com.example.back.repository;

import com.example.back.dto.SelectProductDto;
import com.example.back.entity.SelectProduct;
import java.util.List;

public interface SelectProductCustomRepository {
    List<SelectProductDto> findAllByUserId(Long userId, String period);
}
