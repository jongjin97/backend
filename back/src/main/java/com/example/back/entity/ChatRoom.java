package com.example.back.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 채팅방 테이블
 */

@Entity
@Table(name = "chat_room")
@NoArgsConstructor
@Getter
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(nullable = false)
    private String sellerStatus; //판매 유저 기준 (N : 삭제 방 Y : 있는 방)

    @Column(nullable = false)
    private String buyerStatus; //구매 유저 기준 (N : 삭제 방 Y : 있는 방)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private User sellUser; //판매 유저

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private User buyUser; //구매 유저

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ChatRoom(User sellUser, User buyUser, Product product) {

//        ChatRoom chatRoom;
//        chatRoom = ChatRoom.builder()
//                .sellUser(sellUser)
//                .buyUser(buyUser)
//                .product(product)
//                .build();
        this.sellUser = sellUser;
        this.buyUser = buyUser;
        this.product = product;
        this.buyerStatus="Y";
        this.sellerStatus="Y";
    }
}
