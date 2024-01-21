package com.example.back.dto;

import com.example.back.entity.User;
import com.example.back.entity.UserInfo;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUserInfoDto {

    private Long id;

    private String phoneNum; //전화번호

    private String usrNickName; //닉네임

    private String imgUrl; //프로필 이미지


    private static ModelMapper modelMapper = new ModelMapper();


    public static RequestUserInfoDto of(UserInfo userInfo) {
        return modelMapper.map(userInfo, RequestUserInfoDto.class);
    }
}