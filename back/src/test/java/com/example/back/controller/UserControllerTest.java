package com.example.back.controller;

import com.example.back.config.SecurityConfig;
import com.example.back.config.TestSecurityConfig;
import com.example.back.config.auth.PrincipalDetail;
import com.example.back.constant.Role;
import com.example.back.dto.ResponseUserInfoDto;
import com.example.back.dto.UserDto;
import com.example.back.entity.User;
import com.example.back.jpa.service.UserService;
import com.example.back.mybatis.mapper.UserInfoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserInfoMapper userInfoMapper;
    @InjectMocks
    private UserControllerTest userControllerTest;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private UserDto testUserDto;
    private UserDto expectedResult;
    private User user;
    @Test
    void loginUser() throws Exception { // Arrange
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    when(userService.loginUser(any(UserDto.class))).thenReturn(expectedResult);

    mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUserDto)))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"));
}

    @Test
    void postUser() throws Exception {
        when(userService.createUser(testUserDto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/user/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateUser() throws Exception {
        PrincipalDetail principalDetail = mock(PrincipalDetail.class);
        principalDetail.setUser(user);
        when(userService.updateUser(testUserDto, principalDetail)).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @BeforeEach
    public void setUp(){
        testUserDto =  UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .password("test123")
                .nickname("TestUser")
                .status("Y")
                .provider("user")
                .role(Role.USER)
                .token("token123")
                .userInfo(ResponseUserInfoDto.builder()
                        .id(1L)
                        .phoneNum("010-1234-5678")
                        .usrNickName("TestUser")
                        .imgUrl("/images/user/profile.png")
                        .build())
                .build();
        user = User.builder()
                .email("test@example.com")
                .password("test123")
                .nickname("TestUser")
                .status("Y")
                .provider("user")
                .role(Role.USER)
                .build();
        expectedResult = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .password("test123")
                .nickname("TestUser")
                .status("Y")
                .provider("user")
                .role(Role.USER)
                .token("token123")
                .userInfo(ResponseUserInfoDto.builder()
                        .id(1L)
                        .phoneNum("010-1234-5678")
                        .usrNickName("TestUser")
                        .imgUrl("/images/user/profile.png")
                        .build())
                .build();
    }
}