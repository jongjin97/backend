package com.example.back.repository;


import com.example.back.dto.MainProductDto;
import com.example.back.dto.ProductSearchDto;
import com.example.back.dto.QMainProductDto;
import com.example.back.entity.QProduct;
import com.example.back.entity.QProductImage;
import com.example.back.entity.QPurchaseHistory;
import com.example.back.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MainProductDto> findAllProductAndImgUrl(ProductSearchDto productSearchDto) {

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;
        return queryFactory.select(
                        new QMainProductDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                product.pdCategory,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                user.id,
                                user.nickname)
                )
                .from(productImage)
                .join(productImage.product, product)
                .join(product.user, user)
                .where(productImage.repImgYn.eq("Y"))
                .where(pdTitleLike(productSearchDto.getSearchQuery()))
                .orderBy(product.id.desc())
                .fetch();
    }

    @Override
    public List<MainProductDto> findAllProductByUser(Long id) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;
        return queryFactory.select(
                        new QMainProductDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                product.pdCategory,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                user.id,
                                user.nickname)
                )
                .from(productImage)
                .join(productImage.product, product)
                .join(product.user, user)
                .where(productImage.repImgYn.eq("Y"))
                .where(product.user.id.eq(id))
                .orderBy(product.id.desc())
                .fetch();
    }

    @Override
    public List<MainProductDto> findSearchProductAndImgUrl(String searchQuery) {

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;
        return queryFactory.select(
                        new QMainProductDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                product.pdCategory,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                user.id,
                                user.nickname)
                )
                .from(productImage)
                .join(productImage.product, product)
                .join(product.user, user)
                .where(productImage.repImgYn.eq("Y"))
                .where(pdTitleLike(searchQuery))
                .orderBy(product.id.desc())
                .fetch();
    }

    @Override
    public List<MainProductDto> findCategoryProduct(String pdCategory) {

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;
        return queryFactory.select(
                        new QMainProductDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                product.pdCategory,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                user.id,
                                user.nickname)
                )
                .from(productImage)
                .join(productImage.product, product)
                .join(product.user, user)
                .where(productImage.repImgYn.eq("Y"))
                .where(QProduct.product.pdCategory.eq(pdCategory))
                .orderBy(product.id.desc())
                .fetch();
    }
    @Override
    public MainProductDto findProductAndImgUrlById(Long id) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;

        return  queryFactory.select(
                        new QMainProductDto(
                                product.id,
                                product.pdTitle,
                                product.pdContents,
                                product.pdCategory,
                                productImage.imgUrl,
                                product.price,
                                product.status,
                                user.id,
                                user.nickname)
                )
                .from(productImage)
                .join(productImage.product, product)
                .join(product.user, user)
                .where(productImage.repImgYn.eq("Y"))
                .where(product.id.eq(id))
                .fetchOne();
    }
    private BooleanExpression pdTitleLike(String searchQuery) {

        return StringUtils.hasText(searchQuery) ? QProduct.product.pdTitle.like("%" + searchQuery + "%") : null;
    }
}
