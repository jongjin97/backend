package com.example.back.repository;

import com.example.back.dto.ChatMessageListDto;
import com.example.back.dto.ChatRoomDto;

import java.util.List;

public interface ChatRoomCustomRepository {
    List<ChatMessageListDto> findChatRoomList(Long userId);
    ChatRoomDto findChatRoom(Long chatId);
    ChatRoomDto findChatRoomByBuyUserIdAndProductId(Long userId, Long productId);
}
