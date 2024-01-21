package com.example.back.dto;

import com.example.back.entity.BoardImage;
import com.example.back.entity.ProductImage;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardImageDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardImageDto of(BoardImage boardImage) {

        return modelMapper.map(boardImage, BoardImageDto.class);
    }
}