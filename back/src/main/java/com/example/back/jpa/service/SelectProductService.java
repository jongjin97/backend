package com.example.back.jpa.service;

import com.example.back.dto.SelectProductDto;
import com.example.back.entity.Product;
import com.example.back.entity.SelectProduct;
import com.example.back.entity.User;
import com.example.back.repository.SelectProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectProductService {
    private final SelectProductRepository selectProductRepository;

    public List<SelectProductDto> getSelectProduct(Long userId, String period) {
        List<SelectProductDto> selectProductDtoList = selectProductRepository.findAllByUserId(userId, period);

        return selectProductDtoList;
    }
    @Transactional
    public void saveSelectProduct(Product product, User user) {
        SelectProduct  selectProduct = selectProductRepository.findByUser_IdAndProduct_Id(user.getId(), product.getId())
                .orElse(new SelectProduct("Y",  user, product));

        selectProductRepository.save(selectProduct);
    }
}
