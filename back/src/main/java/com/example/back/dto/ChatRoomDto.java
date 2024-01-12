package com.example.back.dto;

import com.example.back.entity.ChatMessage;
import com.example.back.entity.ChatRoom;
import com.example.back.entity.Product;
import com.example.back.entity.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {

    private Long chatRoomId;

    private ChatMessage chatMessage;
    private String sellerStatus; //판매 유저 기준 N : 삭제 방 Y : 있는 방
    private String buyerStatus; //구매 유저 기준 N : 삭제 방 Y : 있는 방

    private User sellUser; //판매 유저
    private User buyUser; //구매 유저

    private Product product;

}