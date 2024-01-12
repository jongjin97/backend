package com.example.back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String message;

    private LocalDateTime sendTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public static ChatMessage createChatMessage(ChatRoom chatRoom, String message, User user) {

        ChatMessage chatMessage;
        chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .message(message)
                .sendTime(LocalDateTime.now())
                .user(user)
                .build();

        return chatMessage;
    }
}
