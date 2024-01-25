package com.example.back.repository;

import com.example.back.dto.MainBoardDto;
import com.example.back.dto.ProductSearchDto;

import java.util.List;

public interface BoardCustomRepository {
    List<MainBoardDto> findAllBoardAndImgUrl(ProductSearchDto productSearchDto);

    List<MainBoardDto> findUserBoardAndImgUrl(Long id);
}
