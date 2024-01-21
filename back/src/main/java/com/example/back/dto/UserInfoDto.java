package com.example.back.dto;
import com.example.back.entity.User;
import com.example.back.entity.UserInfo;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {

    private Long id;

    private String phoneNum; //전화번호

    private String usrNickName; //닉네임

    private String imgUrl; //프로필 이미지

    private User user;

    private static ModelMapper modelMapper = new ModelMapper();

    public UserInfoDto(String phoneNum, String usrNickName, String imgUrl) {
        this.phoneNum = phoneNum;
        this.usrNickName = usrNickName;
        this.imgUrl = imgUrl;
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .phoneNum(phoneNum)
                .usrNickName(user.getNickname())
                .status("Y")
                .user(user)
                .build();
    }

    public static UserInfoDto of(UserInfo userInfo) {
        return modelMapper.map(userInfo, UserInfoDto.class);
    }
}
