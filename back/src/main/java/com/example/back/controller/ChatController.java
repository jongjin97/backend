package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.ChatListDto;
import com.example.back.entity.User;
import com.example.back.jpa.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public ChatListDto sendMessage(@DestinationVariable Long roomId, @Payload String message
            , SimpMessageHeaderAccessor headerAccessor/*, @AuthenticationPrincipal PrincipalDetail principalDetail*/) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        User user = principalDetail.getUser();

        chatService.createChatMessage(roomId, message, principalDetail);
        ChatListDto chatListDto = new ChatListDto(message, LocalDateTime.now(), user.getId());
        return chatListDto;
    }

    /*@MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public ChatRoomDto messageHandler(@DestinationVariable String roomId, @Payload String message
            , SimpMessageHeaderAccessor headerAccessor, @AuthenticationPrincipal PrincipalDetail principalDetail) {

        chatService.createChatMessage(roomId);

        System.out.println();
        return message;
    }*/
}
