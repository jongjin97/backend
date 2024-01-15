package com.example.back.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatListDto {

    private Long chatMessageId;

    private String message;

    private LocalDateTime sendTime;

    private Long userId;

    private Long buyUser;

    private Long sellUser;

    @QueryProjection
    public ChatListDto(Long chatMessageId, String message, LocalDateTime sendTime, Long userId, Long buyUser, Long sellUser) {
        this.chatMessageId = chatMessageId;
        this.message = message;
        this.sendTime = sendTime;
        this.userId = userId;
        this.buyUser = buyUser;
        this.sellUser = sellUser;
    }

    public ChatListDto(String message, LocalDateTime sendTime, Long userId){
        this.message = message;
        this.sendTime = sendTime;
        this.userId = userId;
    }
}
