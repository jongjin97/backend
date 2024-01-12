package com.example.back.repository;

import com.example.back.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageCustomRepository{
    @Query(value = "select u.nickname from User u where u.id = :userId")
    String findNickname(Long userId);

}
