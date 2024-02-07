package com.example.back.dto;


import com.example.back.entity.Region;
import com.example.back.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionRequestDto {

    private String status; //N : 삭제한 동네 Y : 등록된 동네
    private String regionName; //지역 명칭
    private String leadStatus; //N : 대표지역 (X) Y : 대표지역(O)
    private String noticeStatus; //기본값 Y
    private User user;

    public Region toEntity() {
        return Region.builder()
                .status("Y")
                .regionName("지역을 등록해주세요")
                .leadStatus("Y")
                .noticeStatus("Y")
                .user(user)
                .build();
    }
}
