package com.example.back.repository;


import com.example.back.dto.MainProductDto;
import com.example.back.dto.ProductSearchDto;
import com.example.back.dto.QMainProductDto;
import com.example.back.entity.QProduct;
import com.example.back.entity.QProductImage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.back.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MainProductDto> findAllProductAndImgUrl(ProductSearchDto productSearchDto) {

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        return queryFactory.select(
                        new QMainProductDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                productImage.imgUrl,
                                product.price)
                )
                .from(productImage)
                .join(productImage.product, product)
                .where(productImage.repImgYn.eq("Y"))
                .where(pdTitleLike(productSearchDto.getSearchQuery()))
                .orderBy(product.id.desc())
                .fetch();
    }

    private BooleanExpression pdTitleLike(String searchQuery) {

        return StringUtils.hasText(searchQuery) ? QProduct.product.pdTitle.like("%" + searchQuery + "%") : null;
    }
}