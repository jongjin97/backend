package com.example.back.repository;

import com.example.back.dto.QPurchaseDto;
import com.example.back.dto.QSelectProductDto;
import com.example.back.dto.SelectProductDto;
import com.example.back.entity.QProduct;
import com.example.back.entity.QProductImage;
import com.example.back.entity.QPurchaseHistory;
import com.example.back.entity.QSelectProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SelectProductCustomRepositoryImpl implements SelectProductCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<SelectProductDto> findAllByUserId(Long userId, String period) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QSelectProduct  selectProduct = QSelectProduct.selectProduct;

        return queryFactory.select(
                        new QSelectProductDto(selectProduct.id,
                                product.id,
                                product.pdTitle,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                selectProduct.regTime)
                ).from(product)
                .join(product.productImages, productImage)
                .join(product.selectProducts, selectProduct)
                .where(productImage.repImgYn.eq("Y"))
                .where(selectProduct.user.id.eq(userId))
                .where(selectProductPeriod(period))
                .orderBy(product.id.desc())
                .fetch();
    }
    private BooleanExpression selectProductPeriod(String searchQuery) {
        LocalDateTime localDateTime = LocalDateTime.now();
        QSelectProduct selectProduct = QSelectProduct.selectProduct;
        if(!StringUtils.hasText(searchQuery))
            return null;
        else if(searchQuery.equals("0"))
            return selectProduct.regTime.after(localDateTime.minusYears(1));
        else if(searchQuery.equals("1"))
            return selectProduct.regTime.after(localDateTime.minusWeeks(1));
        else if(searchQuery.equals("2"))
            return selectProduct.regTime.after(localDateTime.minusMonths(1));
        else if(searchQuery.equals("3"))
            return selectProduct.regTime.after(localDateTime.minusMonths(3));
        else if(searchQuery.equals("4"))
            return selectProduct.regTime.after(localDateTime.minusMonths(6));
        return null;
    }
}
