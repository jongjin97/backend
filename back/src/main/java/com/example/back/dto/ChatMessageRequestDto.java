package com.example.back.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {

    private Long chatMessageId;
    private Long chatRoomId;
    private String message;
    private LocalDateTime sendTime;
    private Long userId;

    @QueryProjection
    public ChatMessageRequestDto(Long chatMessageId, Long chatRoomId, String message, LocalDateTime sendTime, Long userId) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.sendTime = sendTime;
        this.userId = userId;
    }
}
