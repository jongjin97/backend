package com.example.back.controller;


import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.ChatMessage;
import com.example.back.jpa.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
    public Long createRoom(@PathVariable Long productId, @AuthenticationPrincipal PrincipalDetail principalDetail
            ,@RequestBody String message) {
        return chatService.createRoom(productId, principalDetail, message);
    }

    // 채팅방 나가기
    @DeleteMapping("/exit/{chatId}")
    public void deleteRoom(@PathVariable Long chatId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        chatService.deleteRoom(chatId, principalDetail.getId());
    }

    @GetMapping("/chatroom")
    public List<ChatMessageListDto> getChatRoomList(@AuthenticationPrincipal PrincipalDetail principalDetail){
        return chatService.findChatRoomList(principalDetail.getId());

    }

    //채팅방 조회
    @GetMapping("/chatroom/{chatId}")
    public ChatRoomDto getChatRoom(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long chatId){
        return chatService.findChatRoom(chatId);
    }
    @GetMapping("/chatroom/product/{productId}")
    public ChatRoomDto getChatRoomByProductId(@AuthenticationPrincipal PrincipalDetail principalDetail
            , @PathVariable Long productId){
        return chatService.findChatRoomByBuyUserIdAndProductId(principalDetail.getId(), productId);
    }
}
