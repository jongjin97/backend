package com.example.back.repository;

import com.example.back.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository{
}