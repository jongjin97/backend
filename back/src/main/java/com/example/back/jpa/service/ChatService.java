package com.example.back.jpa.service;


import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.ChatListDto;
import com.example.back.dto.ChatMessageDto;
import com.example.back.entity.ChatMessage;
import com.example.back.entity.ChatRoom;
import com.example.back.entity.Product;
import com.example.back.entity.User;
import com.example.back.repository.ChatMessageRepository;
import com.example.back.repository.ChatRoomRepository;
import com.example.back.repository.ProductRepository;
import com.example.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessageSendingOperations template;
    private final ProductRepository productRepository;


    public String findNickname(PrincipalDetail principalDetail) {

        User user = userRepository.findById(principalDetail.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: "));

        String nickname = chatMessageRepository.findNickname(user.getId());


        return nickname;
    }

    public void removeChatUser(Long userId, Long channelId) {

        ChatMessage chatMessage = chatMessageRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("channel not found with ID: "));

        String nickname = chatMessageRepository.findNickname(userId);

        if (nickname != null) {
            log.info("User Disconnected : " + nickname);

            // builder 어노테이션 활용
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .userId(chatMessage.getUser().getId())
                    .message(nickname + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + channelId, chatMessageDto);
        }

        chatMessageRepository.deleteById(channelId);
    }

    public Long createRoom(Long productId, PrincipalDetail principalDetail) {

        //구매 유저 조회
        User buyUser = userRepository.findById(principalDetail.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: "));

        //판매자 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: "));

        //판매자 상품 id로 판매자 User id 조회
        User sellUser = productRepository.findByUserId(product.getId());

        ChatRoom chatRoom = ChatRoom.createChatRoom(sellUser, buyUser, product);

        chatRoomRepository.save(chatRoom);

        return productId;
    }

    public void createChatMessage(Long roomId, String message, PrincipalDetail principalDetail) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with ID: "));

        User user = userRepository.findById(principalDetail.getId()).orElseThrow(() -> new IllegalArgumentException("User not found with ID: "));

        ChatMessage result = ChatMessage.createChatMessage(chatRoom, message, user);

        chatMessageRepository.save(result);

    }

    public List<ChatListDto> getChatRoomMessage(Long chatRoomId) {
        return chatMessageRepository.findChatList(chatRoomId);
    }

   /* public ChatMessageDto getChatRoomMessage(PrincipalDetail principalDetail) {

        chatMessageRepository.
    }*/

}