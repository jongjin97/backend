package com.example.back.dto;

import com.example.back.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDto {
    private Long id;
    private String nickname;
    private ResponseUserInfoDto userInfo;
    @QueryProjection
    public ResponseUserDto(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        if (user.getUserInfo() != null) {
            this.userInfo = new ResponseUserInfoDto(user.getUserInfo());
        } else {
            this.userInfo = null;
        }
    }

}
