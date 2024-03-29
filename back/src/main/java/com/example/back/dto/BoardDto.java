package com.example.back.dto;

import com.example.back.entity.Board;
import com.example.back.entity.Region;
import com.example.back.entity.User;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long userId;

    private Long regionId;

    private String status;

    private String bdSubject; //게시글 주제

    private String bdContents; //게시글 내용

    public BoardDto(Board board, User user, Region region) {
        this.userId = user.getId();
        this.regionId = region.getId();
        this.status = board.getStatus();
        this.bdSubject = board.getBdSubject();
        this.bdContents = board.getBdContents();
    }
}
