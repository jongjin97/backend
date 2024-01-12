package com.example.back.controller;


import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.ChatListDto;
import com.example.back.dto.ChatMessageDto;
import com.example.back.dto.ChatRoomDto;
import com.example.back.entity.ChatMessage;
import com.example.back.jpa.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatService chatService;

    // 채팅방 메시지 조회
    @GetMapping("/{chatRoomId}")
    public List<ChatListDto> getChatRoomMessage(@PathVariable Long chatRoomId) {

        return chatService.getChatRoomMessage(chatRoomId);
    }

    // 채팅방 생성
    @PostMapping("/createroom/{productId}")
    public Long createRoom(@PathVariable Long productId, @AuthenticationPrincipal PrincipalDetail principalDetail) {

        return chatService.createRoom(productId, principalDetail);
    }
}