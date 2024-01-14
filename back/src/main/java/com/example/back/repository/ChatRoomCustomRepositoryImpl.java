package com.example.back.repository;

import com.example.back.dto.ChatRoomDto;
import com.example.back.dto.QChatRoomDto;
import com.example.back.dto.QMainProductDto;
import com.example.back.dto.QResponseUserDto;
import com.example.back.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ChatRoomDto> findChatRoomList(Long userId){
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QProductImage productImage = QProductImage.productImage;
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
                , chatRoom.product.status)))
                .from(chatRoom)
                .join(chatRoom.product.productImages, productImage)
                .where(chatRoom.sellUser.id.eq(userId).or(chatRoom.buyUser.id.eq(userId)))
                .where(productImage.repImgYn.eq("Y"))
                .fetch();


    }
}
