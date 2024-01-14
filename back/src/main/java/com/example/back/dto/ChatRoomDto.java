package com.example.back.dto;

import com.example.back.entity.ChatMessage;
import com.example.back.entity.ChatRoom;
import com.example.back.entity.Product;
import com.example.back.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {

    private Long chatRoomId;

    private String sellerStatus; //판매 유저 기준 N : 삭제 방 Y : 있는 방
    private String buyerStatus; //구매 유저 기준 N : 삭제 방 Y : 있는 방
    private ResponseUserDto sellUser; //판매 유저
    private ResponseUserDto buyUser; //구매 유저

    private MainProductDto product;

}