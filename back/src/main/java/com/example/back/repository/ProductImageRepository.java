package com.example.back.repository;

import com.example.back.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    ProductImage findByProductIdAndRepImgYn(Long productId, String repImgYn);

    @Query(value = "select pi.id from ProductImage pi where pi.product.id = :id")
    List<Long> countById(Long id);

    List<ProductImage> findByProductIdOrderByIdAsc(Long productId);
}
