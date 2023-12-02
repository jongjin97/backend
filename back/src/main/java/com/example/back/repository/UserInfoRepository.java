package com.example.back.repository;

import com.example.back.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @Query(value = "select ui.id from UserInfo ui where ui.id = :id")
    Long countById(Long id);

    Optional<UserInfo> findByUser_Id(Long id);
}
