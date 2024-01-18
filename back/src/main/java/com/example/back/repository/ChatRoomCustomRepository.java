package com.example.back.repository;

import com.example.back.dto.ChatRoomDto;

import java.util.List;

public interface ChatRoomCustomRepository {
    List<ChatRoomDto> findChatRoomList(Long userId);
    ChatRoomDto findChatRoom(Long chatId);
    ChatRoomDto findChatRoomByBuyUserIdAndProductId(Long userId, Long productId);
}
