package com.example.back.repository;

import com.example.back.dto.ChatListDto;

import java.util.List;

public interface ChatMessageCustomRepository {

    List<ChatListDto> findChatList(Long userId);
}
