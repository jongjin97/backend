package com.example.back.repository;

import com.example.back.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

}