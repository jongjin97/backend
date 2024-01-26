package com.example.back.repository;

import com.example.back.dto.*;
import com.example.back.entity.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ChatMessageListDto> findChatRoomList(Long userId){
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QProductImage productImage = QProductImage.productImage;
        QChatMessage chatMessage = QChatMessage.chatMessage;
        QUser user = QUser.user;
        // Userinfo query2개 생김
        return queryFactory.select(new QChatMessageListDto(chatRoom.id, chatRoom.sellerStatus, chatRoom.buyerStatus
                        , new QResponseUserDto(chatRoom.sellUser)
                        , new QResponseUserDto(chatRoom.buyUser)
                        , new QMainProductDto(chatRoom.product.id
                        , chatRoom.product.pdTitle
                , chatRoom.product.pdContents
                , chatRoom.product.pdCategory
                , productImage.imgUrl
                , chatRoom.product.price
                , chatRoom.product.status
                        , chatRoom.product.user.id
                        , chatRoom.product.user.nickname)
                        , new QChatMessageRequestDto(
                        chatMessage.id,
                        chatMessage.chatRoom.id,
                        chatMessage.message,
                        chatMessage.sendTime,
                        chatMessage.user.id
                )))
                .from(chatMessage)
                .join(chatMessage.chatRoom.product.productImages, productImage)
                .join(chatMessage.chatRoom, chatRoom)
                .join(chatMessage.user, user)
                .where(chatRoom.sellUser.id.eq(userId).or(chatRoom.buyUser.id.eq(userId)))
                .where(productImage.repImgYn.eq("Y"))
                .where(chatMessage.id.in(
                        JPAExpressions
                                .select(chatMessage.id.max())
                                .from(chatMessage)
                                .groupBy(chatMessage.chatRoom.id)))
                .fetch();


    }

    @Override
    public ChatRoomDto findChatRoom(Long chatId) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;
        // Userinfo query2개 생김
        return queryFactory.select(new QChatRoomDto(chatRoom.id, chatRoom.sellerStatus, chatRoom.buyerStatus
                        , new QResponseUserDto(chatRoom.sellUser)
                        , new QResponseUserDto(chatRoom.buyUser)
                        , new QMainProductDto(chatRoom.product.id
                        , chatRoom.product.pdTitle
                        , chatRoom.product.pdContents
                        , chatRoom.product.pdCategory
                        , productImage.imgUrl
                        , chatRoom.product.price
                        , chatRoom.product.status
                        , user.id
                        , user.nickname)))
                .from(chatRoom)
                .join(chatRoom.product.productImages, productImage)
                .join(chatRoom.product.user, user)
                .where(chatRoom.id.eq(chatId))
                .where(productImage.repImgYn.eq("Y"))
                .fetchOne();
    }

    @Override
    public ChatRoomDto findChatRoomByBuyUserIdAndProductId(Long userId, Long productId) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QProductImage productImage = QProductImage.productImage;
        QUser user = QUser.user;
        // Userinfo query2개 생김
        return queryFactory.select(new QChatRoomDto(chatRoom.id, chatRoom.sellerStatus, chatRoom.buyerStatus
                        , new QResponseUserDto(chatRoom.sellUser)
                        , new QResponseUserDto(chatRoom.buyUser)
                        , new QMainProductDto(chatRoom.product.id
                        , chatRoom.product.pdTitle
                        , chatRoom.product.pdContents
                        , chatRoom.product.pdCategory
                        , productImage.imgUrl
                        , chatRoom.product.price
                        , chatRoom.product.status
                        , user.id
                        , user.nickname)))
                .from(chatRoom)
                .join(chatRoom.product.productImages, productImage)
                .join(chatRoom.product.user, user)
                .where(chatRoom.buyUser.id.eq(userId))
                .where(chatRoom.product.id.eq(productId))
                .where(productImage.repImgYn.eq("Y"))
                .fetchOne();
    }
}
