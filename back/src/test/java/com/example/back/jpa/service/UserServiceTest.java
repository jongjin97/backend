package com.example.back.jpa.service;

import com.example.back.config.JwtProvider;
import com.example.back.constant.Role;
import com.example.back.dto.UserDto;
import com.example.back.entity.User;
import com.example.back.entity.UserInfo;
import com.example.back.mybatis.mapper.UserInfoMapper;
import com.example.back.repository.UserInfoRepository;
import com.example.back.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserInfoRepository userInfoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Test
    void createUser() {
        Faker faker = new Faker();
        UserDto userDto = UserDto.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .status("Y")
                .nickname(faker.name().name())
                .build();
        User user = userDto.toEntity();

        // Mocking userRepository.save
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);

        // Mocking userInfoRepository.save
        given(userInfoRepository.save(Mockito.any(UserInfo.class))).willReturn(new UserInfo("Y", "010-1234-5678", "nick", "url"));

        // Mocking passwordEncoder.encode
        given(passwordEncoder.encode(Mockito.anyString())).willReturn("encodedPassword");

        // When
        User createdUser = userService.createUser(userDto);

        // Then
        // Verify that userRepository.save is called once
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

        // Verify that userInfoRepository.save is called once
        Mockito.verify(userInfoRepository, Mockito.times(1)).save(Mockito.any(UserInfo.class));
        assertEquals(createdUser.getEmail(), userDto.getEmail());
    }
    
}