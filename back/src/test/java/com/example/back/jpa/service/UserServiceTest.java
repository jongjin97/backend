package com.example.back.jpa.service;

import com.example.back.config.JwtProvider;
import com.example.back.config.auth.PrincipalDetail;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserInfoService userInfoService;
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
        verify(userRepository, times(1)).save(Mockito.any(User.class));

        // Verify that userInfoRepository.save is called once
        verify(userInfoRepository, times(1)).save(Mockito.any(UserInfo.class));
        assertEquals(createdUser.getEmail(), userDto.getEmail());
    }

    @Test
    void deleteUser() throws Exception {
        // Arrange
        User testUser = User.builder()
                .email("test@example.com")
                .password("test123")
                .nickname("TestUser")
                .status("Y")
                .provider("google")
                .providerId("123456789")
                .role(Role.USER)
                .userInfo(UserInfo.builder()
                        .id(100L)
                        .phoneNum("010-1234-5678")
                        .usrNickName("TestUser")
                        .imgUrl("/images/user/profile.png")
                        .build())
                .build();

        PrincipalDetail principalDetail = new PrincipalDetail(testUser);
        when(userRepository.findById(principalDetail.getId())).thenReturn(Optional.of(testUser));
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.of(testUser.getUserInfo()));
        // Act
        userService.deleteUser(principalDetail);

        // Assert
        verify(userInfoService, times(1)).deleteProfileImg(testUser.getUserInfo().getId());
        verify(userRepository, times(1)).deleteById(principalDetail.getId());
    }
}