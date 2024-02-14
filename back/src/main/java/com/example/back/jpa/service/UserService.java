package com.example.back.jpa.service;


import com.example.back.config.JwtProvider;
import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.ResponseUserInfoDto;
import com.example.back.dto.UserDto;
import com.example.back.dto.UserInfoDto;
import com.example.back.entity.User;
import com.example.back.entity.UserInfo;
import com.example.back.mybatis.mapper.UserInfoMapper;
import com.example.back.repository.UserInfoRepository;
import com.example.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoService userInfoService;
    private final UserInfoMapper userInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //계정 생성
    public User createUser(UserDto userDto) {

        userDto.encodePassword(passwordEncoder);
        User user = userDto.toEntity();

        //이메일 중복 검사
        validateDuplicateMember(user);
        user = userRepository.save(user);

        //User findUser = userRepository.findById(user.getId()).orElseThrow(EntityNotFoundException::new);

        UserInfo userInfo = UserInfoDto.builder()
                .phoneNum("")
                .usrNickName(userDto.getNickname())
                .user(user)
                .build().toEntity();


        userInfoRepository.save(userInfo);

        return user;
    }

    //계정 정보 수정
    @Transactional
    public Long updateUser(UserDto userDto, PrincipalDetail principalDetail) {
        User user = userRepository.findByEmail(principalDetail.getEmail());

        System.out.println("현재 유저 EMAIL= " + user.getEmail());
        System.out.println("현재 유저 ID = " + user.getId());

        user.update(passwordEncoder.encode(userDto.getPassword()), userDto.getNickname());

        userRepository.save(user);

        if(principalDetail != null) {
            principalDetail.setUser(user); //추가
        }

        return user.getId();
    }

    //계정 삭제
    @Transactional
    public void deleteUser(PrincipalDetail principalDetail) throws Exception {

        User user = userRepository.findById(principalDetail.getId()).orElseThrow(EntityNotFoundException::new);
        UserInfo userInfo = userInfoRepository.findById(user.getUserInfo().getId()).orElseThrow(EntityNotFoundException::new);

        //프로필 이미지 삭제
        userInfoService.deleteProfileImg(userInfo.getId());

        userRepository.deleteById(principalDetail.getId());
    }

    private void validateDuplicateMember(User user) {

        User findUser = userRepository.findByEmail(user.getEmail());
        if(findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public UserDto loginUser(UserDto userDto) {

        User user = userRepository.findByEmail(userDto.getEmail());
        if(user == null) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");

        }

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        /*User result = userRepository.findUser(userDto.getEmail());*/

        UserDto result = UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .provider("user")
                .role(user.getRole())
                .token(jwtProvider.createToken(user.getEmail(), user.getRole()))
                .build();
        if(user.getUserInfo() != null){
            result.setUserInfo(new ResponseUserInfoDto(user.getUserInfo()));
        }
        return result;
    }


    public UserInfo createUserInfo(UserInfoDto userInfoDto, MultipartFile profileImg, PrincipalDetail principalDetail) throws Exception {

        User user = userRepository.findById(principalDetail.getId()).orElseThrow(EntityNotFoundException::new);

       /* if(id == null) {
            throw new IllegalStateException("존재하지 않는 닉네임입니다.");
        }*/

        UserInfo userInfo = UserInfoDto.builder()
                .phoneNum(userInfoDto.getPhoneNum())
                .usrNickName(principalDetail.getNickName())
                .user(user)
                .build().toEntity();

        //유저 상세 정보 등록
        userInfoService.saveProfileImg(userInfo, profileImg);

        return userInfoRepository.save(userInfo);
    }


    public UserInfo updateUserInfo(UserInfoDto userInfoDto, MultipartFile profileImg, PrincipalDetail principalDetail) throws Exception {

        UserInfo userInfo = userInfoRepository.findById(principalDetail.getId()).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));

        userInfo.updateUserInfo(userInfoDto.getPhoneNum(), userInfoDto.getUsrNickName());

        Long profileImgIds = userInfoRepository.countById(principalDetail.getId());

        userInfoService.updateProfileImg(profileImgIds, profileImg);

        return userInfoRepository.save(userInfo);
    }
    @Transactional
    public ResponseUserInfoDto createUserInfov2(UserInfoDto userInfoDto, MultipartFile profileImg, PrincipalDetail principalDetail) throws Exception {
        User user = userRepository.findById(principalDetail.getId()).orElseThrow(EntityNotFoundException::new);
        UserInfo userInfo = userInfoRepository.findByUser_Id(principalDetail.getId()).orElse(UserInfoDto.builder()
                .phoneNum(userInfoDto.getPhoneNum())
                .usrNickName(userInfoDto.getUsrNickName())
                .user(user)
                .build().toEntity());
        UserInfo savedUserInfo;
        userInfo.setUsrNickName(userInfoDto.getUsrNickName());
        if(profileImg != null)
            savedUserInfo = userInfoService.saveProfileImgv2(userInfo, profileImg);
        else
            savedUserInfo = userInfo;
        ResponseUserInfoDto responseUserInfoDto = ResponseUserInfoDto.builder()
                .id(savedUserInfo.getId())
                .phoneNum(savedUserInfo.getPhoneNum())
                .imgUrl(savedUserInfo.getImgUrl())
                .usrNickName(savedUserInfo.getUsrNickName())
                .build();
        return responseUserInfoDto;
    }
}
