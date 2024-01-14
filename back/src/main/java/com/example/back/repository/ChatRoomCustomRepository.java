package com.example.back.repository;

import com.example.back.dto.ChatRoomDto;

import java.util.List;

public interface ChatRoomCustomRepository {
    List<ChatRoomDto> findChatRoomList(Long userId);
}
