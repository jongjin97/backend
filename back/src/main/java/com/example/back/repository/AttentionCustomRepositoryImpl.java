package com.example.back.repository;

import com.example.back.dto.*;
import com.example.back.entity.QAttention;
import com.example.back.entity.QProduct;
import com.example.back.entity.QProductImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class AttentionCustomRepositoryImpl implements AttentionCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AttentionRequestDto> findProductAndImgUrl(Long userId) {

        QAttention attention = QAttention.attention;
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;


        return queryFactory.select(
                        new QAttentionRequestDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                product.pdCategory,
                                productImage.imgUrl,
                                product.price,
                                product.status)
                )
                .from(product)
                .join(productImage).on(product.id.eq(productImage.product.id))
                .join(attention).on(attention.product.id.eq(product.id))
                .where(productImage.repImgYn.eq("Y"))
                .where(attention.status.eq("Y"))
                .where(attention.user.id.eq(userId))
                .fetch();
    }
}
