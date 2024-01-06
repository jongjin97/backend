package com.example.back.repository;

import com.example.back.dto.PurchaseDto;
import com.example.back.dto.QPurchaseDto;
import com.example.back.entity.QProduct;
import com.example.back.entity.QProductImage;
import com.example.back.entity.QPurchaseHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class PurchaseHistoryCustomRepositoryImpl implements PurchaseHistoryCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PurchaseDto> findSaleHistory(Long id, String title, String period) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QPurchaseHistory purchaseHistory = QPurchaseHistory.purchaseHistory;
        return queryFactory.select(
                new QPurchaseDto(purchaseHistory.id,
                        product.id,
                        product.pdTitle,
                        productImage.imgUrl,
                        product.price,
                        product.status,
                        purchaseHistory.regTime)
                ).from(product)
                .join(product.productImages, productImage)
                .join(product.purchaseHistory, purchaseHistory)
                .where(productImage.repImgYn.eq("Y"))
                .where(product.user.id.eq(id))
                .where(pdTitleLike(title))
                .where(pdPeriod(period))
                .orderBy(product.id.desc())
                .fetch();
    }

    @Override
    public List<PurchaseDto> findPurchaseHistory(Long id, String title, String period) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QPurchaseHistory purchaseHistory = QPurchaseHistory.purchaseHistory;
        return queryFactory.select(
                        new QPurchaseDto(purchaseHistory.id,
                                product.id,
                                product.pdTitle,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                purchaseHistory.regTime)
                ).from(product)
                .join(product.productImages, productImage)
                .join(product.purchaseHistory, purchaseHistory)
                .where(productImage.repImgYn.eq("Y"))
                .where(purchaseHistory.user.id.eq(id))
                .where(pdTitleLike(title))
                .where(pdPeriod(period))
                .orderBy(product.id.desc())
                .fetch();
    }
    private BooleanExpression pdTitleLike(String searchQuery) {
        return StringUtils.hasText(searchQuery) ? QProduct.product.pdTitle.like("%" + searchQuery + "%") : null;
    }
    private BooleanExpression pdPeriod(String searchQuery) {
        LocalDateTime localDateTime = LocalDateTime.now();
        QPurchaseHistory purchaseHistory = QPurchaseHistory.purchaseHistory;
        if(!StringUtils.hasText(searchQuery))
            return null;
        else if(searchQuery.equals("0"))
            return purchaseHistory.regTime.after(localDateTime.minusYears(1));
        else if(searchQuery.equals("1"))
            return purchaseHistory.regTime.after(localDateTime.minusWeeks(1));
        else if(searchQuery.equals("2"))
            return purchaseHistory.regTime.after(localDateTime.minusMonths(1));
        else if(searchQuery.equals("3"))
            return purchaseHistory.regTime.after(localDateTime.minusMonths(3));
        else if(searchQuery.equals("4"))
            return purchaseHistory.regTime.after(localDateTime.minusMonths(6));
        return null;
    }

}
