package com.example.back.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainBoardDto {

    private Long boardId;
    private String regionName; //동네 이름
    private String bdSubject; //게시글 주제
    private String bdContents; //게시글 내용
    private String imgUrl; //이미지 경로
    private String status; //게시글 상태

    @QueryProjection
    public MainBoardDto(Long boardId, String regionName, String bdSubject, String bdContents, String imgUrl, String status) {
        this.boardId = boardId;
        this.regionName = regionName;
        this.bdSubject = bdSubject;
        this.bdContents = bdContents;
        this.imgUrl = imgUrl;
        this.status = status;
    }
}
