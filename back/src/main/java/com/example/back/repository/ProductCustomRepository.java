package com.example.back.repository;

import com.example.back.dto.MainProductDto;
import com.example.back.dto.ProductSearchDto;

import java.util.List;

public interface ProductCustomRepository {
    List<MainProductDto> findAllProductAndImgUrl(ProductSearchDto productSearchDto);

    List<MainProductDto> findSearchProductAndImgUrl(String searchQuery);
    List<MainProductDto> findAllProductByUser(Long id);

    List<MainProductDto> findCategoryProduct(String pdCategory);

    MainProductDto findProductAndImgUrlById(Long id);
}
