package com.example.back.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MainProductDto {

    private Long product_id;
    private String pdTitle; //상품 제목
    private String pdContents; //상품 내용
    private String imgUrl; //이미지 경로
    private String price; //가격
    private String pdStatus; //상품 상태
    @QueryProjection
    public MainProductDto(Long product_id, String pdTitle, String pdContents, String imgUrl, String price, String pdStatus) {
        this.product_id = product_id;
        this.pdTitle = pdTitle;
        this.pdContents = pdContents;
        this.imgUrl = imgUrl;
        this.price = price;
        this.pdStatus = pdStatus;
    }
}
