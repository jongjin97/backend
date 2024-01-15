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
@Builder
public class ChatRoomDto {

    private Long chatRoomId;

    private String sellerStatus; //판매 유저 기준 N : 삭제 방 Y : 있는 방
    private String buyerStatus; //구매 유저 기준 N : 삭제 방 Y : 있는 방
    private ResponseUserDto sellUser; //판매 유저
    private ResponseUserDto buyUser; //구매 유저

    private MainProductDto product;

    @QueryProjection
    public ChatRoomDto(Long chatRoomId, String sellerStatus, String buyerStatus, ResponseUserDto sellUser, ResponseUserDto buyUser, MainProductDto product) {
        this.chatRoomId = chatRoomId;
        this.sellerStatus = sellerStatus;
        this.buyerStatus = buyerStatus;
        this.sellUser = sellUser;
        this.buyUser = buyUser;
        this.product = product;
    }
}