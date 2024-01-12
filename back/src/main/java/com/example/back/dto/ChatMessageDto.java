package com.example.back.dto;


import com.example.back.entity.ChatMessage;
import com.example.back.entity.ChatRoom;
import com.example.back.entity.Product;
import com.example.back.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    private Long chatMessageId;

    private ChatRoom chatRoom;
    private String message;
    private LocalDateTime sendTime;
    private Long userId;

}