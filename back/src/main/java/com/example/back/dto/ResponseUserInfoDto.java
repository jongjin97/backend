package com.example.back.dto;

import com.example.back.entity.UserInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserInfoDto {
    private Long id;

    private String phoneNum; //전화번호

    private String usrNickName; //닉네임

    private String imgUrl; //프로필 이미지

    public ResponseUserInfoDto(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.phoneNum = userInfo.getPhoneNum();
        this.imgUrl = userInfo.getImgUrl();
        this.usrNickName = userInfo.getUsrNickName();
    }
}
