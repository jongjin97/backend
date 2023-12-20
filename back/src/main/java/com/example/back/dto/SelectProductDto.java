package com.example.back.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SelectProductDto {
    private Long id;
    private String pdTitle; //상품 제목
    private String imgUrl; //이미지 경로
    private String price; //가격
    private String pdStatus; //상품 상태
    private LocalDateTime regTime; //등록 시간

    @QueryProjection
    public SelectProductDto(Long id, String pdTitle, String imgUrl, String price, String pdStatus, LocalDateTime regTime) {
        this.id = id;
        this.pdTitle = pdTitle;
        this.imgUrl = imgUrl;
        this.price = price;
        this.pdStatus = pdStatus;
        this.regTime = regTime;
    }
}
