package com.example.back.jpa.service;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.*;
import com.example.back.repository.AttentionRepository;
import com.example.back.repository.ProductImageRepository;
import com.example.back.repository.ProductRepository;
import com.example.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttentionService {

    private final AttentionRepository attentionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;

    //Attention 삽입, 수정 메소드
    @Transactional
    public AttentionDto saveAndUpdateAttention(PrincipalDetail principalDetail, AttentionDto attentionDto) {

        //userId로 UserEntity 조회, 없으면 예외처리
        User user = userRepository.findById(principalDetail.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + attentionDto.getUserId()));

        //productId로 ProductEntity 조회, 없으면 예외처리
        Product product = productRepository.findById(attentionDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + attentionDto.getProductId()));

        //userEntity, productEntity로 AttentionEntity 조회, 없으면 생성
        Attention attention = attentionRepository.findByUserAndProduct(user, product)
                .orElse(Attention.builder()
                        .user(user)
                        .product(product)
                        .build());

        //AttentionEntity의 status 수정
        attention.setStatus(attentionDto.getStatus());
        //AttentionEntity 저장
        attentionRepository.save(attention);

        return AttentionDto.builder()
                .status(attention.getStatus())
                .userId(user.getId())
                .productId(product.getId()).build();
    }

    //User의 RegionList 조회
    @Transactional
    public List<AttentionDto> selectAttentionListByUserId(long userId) {
        //userId로 UserEntity 조회, 없으면 예외 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        //retionList 없을 때 빈 리스트 반환
        List<Attention> attentionList = attentionRepository.findAllByUser(user).orElse(Collections.emptyList());
        List<AttentionDto> attentionDtoList = new ArrayList<>();
        //regionList를 regionDtoList로 변환
        for (Attention attention : attentionList) {
            attentionDtoList.add(
                    AttentionDto.builder()
                            .status(attention.getStatus())
                            .userId(attention.getUser().getId())
                            .productId(attention.getProduct().getId())
                            .build()
            );
        }
        return attentionDtoList;
    }

    public List<AttentionRequestDto> getAttentionListByStatusY(Long userId) {

        //userId로 UserEntity 조회, 없으면 예외 처리
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));


        List<AttentionRequestDto> productList = attentionRepository.findProductAndImgUrl(userId);

        return productList;
    }

    public List<AttentionRequestDto> getAttentionList(String searchQuery, Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));


        List<AttentionRequestDto> productList = attentionRepository.findLikeProductOfPdTitle(searchQuery, userId);

        return productList;
    }
}
