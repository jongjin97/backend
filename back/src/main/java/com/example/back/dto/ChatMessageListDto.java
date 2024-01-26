package com.example.back.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ChatMessageListDto {

    private Long chatRoomId;

    private String sellerStatus; //판매 유저 기준 N : 삭제 방 Y : 있는 방
    private String buyerStatus; //구매 유저 기준 N : 삭제 방 Y : 있는 방
    private ResponseUserDto sellUser; //판매 유저
    private ResponseUserDto buyUser; //구매 유저

    private MainProductDto product;
    private ChatMessageRequestDto chatMessage;

    @QueryProjection
    public ChatMessageListDto(Long chatRoomId, String sellerStatus, String buyerStatus, ResponseUserDto sellUser, ResponseUserDto buyUser, MainProductDto product, ChatMessageRequestDto chatMessage) {
        this.chatRoomId = chatRoomId;
        this.sellerStatus = sellerStatus;
        this.buyerStatus = buyerStatus;
        this.sellUser = sellUser;
        this.buyUser = buyUser;
        this.product = product;
        this.chatMessage = chatMessage;
    }
}