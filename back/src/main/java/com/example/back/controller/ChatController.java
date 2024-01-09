package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.entity.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketMessage;

import java.security.Principal;

@RestController
public class ChatController {

    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public String sendMessage(@DestinationVariable String roomId, @Payload String message
            , SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
        User user = principalDetail.getUser();
        User user2 = ((PrincipalDetail) authentication.getPrincipal()).getUser();
        System.out.println();
        return message;
    }
}
