package com.example.back.repository;

import com.example.back.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Query(value = "select bi.id from BoardImage bi where bi.board.id = :id")
    List<Long> countById(Long id);
}
