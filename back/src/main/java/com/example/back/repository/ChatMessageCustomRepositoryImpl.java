package com.example.back.repository;


import com.example.back.dto.ChatListDto;
import com.example.back.dto.QChatListDto;
import com.example.back.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository {

    private final JPAQueryFactory queryFactory;



    @Override
    public List<ChatListDto> findChatList(Long chatRoomId) {

        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatMessage chatMessage = QChatMessage.chatMessage;


        return queryFactory.select(
                        new QChatListDto(
                                chatMessage.id,
                                chatMessage.message,
                                chatMessage.sendTime,
                                chatMessage.user.id,
                                chatRoom.buyUser.id,
                                chatRoom.sellUser.id
                        )
                )
                .from(chatMessage)
                .join(chatRoom).on(chatMessage.chatRoom.id.eq(chatRoom.id))
                .where(chatRoom.id.eq(chatRoomId))
                .orderBy(chatMessage.sendTime.asc())
                .fetch();
    }
}
