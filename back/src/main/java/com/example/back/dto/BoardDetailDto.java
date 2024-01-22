package com.example.back.dto;


import com.example.back.entity.Board;
import com.example.back.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailDto {

    private Long userId;
    private Long regionId;
    private String status;
    private String bdSubject; //게시글 주제
    private String bdContents; //게시글 내용

    private static ModelMapper modelMapper = new ModelMapper();
    private List<BoardImageDto> boardImageDtoList = new ArrayList<>();
    private List<RequestUserInfoDto> userInfoDtoList = new ArrayList<>();
    private List<RegionDto> regionDtoList = new ArrayList<>();

    public static BoardDetailDto of(Board board) {
        return modelMapper.map(board, BoardDetailDto.class);
    }
}
