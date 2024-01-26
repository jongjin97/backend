package com.example.back.dto;

import com.example.back.entity.Product;
import com.example.back.entity.Region;
import com.example.back.entity.User;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ProductDto {

    @NotEmpty(message = "상품 제목은 필수 입력 값 입니다.")
    private String pdTitle; //상품 제목

    @NotEmpty(message = "상품 내용은 필수 입력 값 입니다.")
    private String pdContents; //상품 내용

    private String pdCategory; //카테고리 별 상품 분류

    @NotEmpty(message = "상품 가격은 필수 입력 값 입니다.")
    private String price; //상품 가격

    private String status; //N: 상품 없음 C: 거래 완료 R: 예약중 Y: 상품 있음

    private String hideStatus; //판매 완료된 상품 중 N: 숨기지 않음 Y: 숨김

    private Long userId;

    private Long regionId;

    private static ModelMapper modelMapper = new ModelMapper();
    private List<Long> productImgIds = new ArrayList<>();
    private List<ProductImageDto> productImageDtoList = new ArrayList<>();
    private List<RequestUserInfoDto> userInfoDtoList = new ArrayList<>();
    private List<RegionDto> regionDtoList = new ArrayList<>();

    public ProductDto(Product product, Region region, User user) {
        this.userId = user.getId();
        this.regionId = region.getId();
        this.pdTitle = product.getPdTitle();
        this.pdContents = product.getPdContents();
        this.pdCategory = product.getPdCategory();
        this.price = product.getPrice();
        this.status = "Y"; //상품 있음
        this.hideStatus = "N"; //숨기지 않음
    }

    public static ProductDto of(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }


}
