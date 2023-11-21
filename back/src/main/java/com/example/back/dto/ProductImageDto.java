package com.example.back.dto;

import com.example.back.entity.ProductImage;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ProductImageDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImageDto of(ProductImage productImage) {

        return modelMapper.map(productImage, ProductImageDto.class);
    }
}
