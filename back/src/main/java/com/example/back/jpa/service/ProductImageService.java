package com.example.back.jpa.service;


import com.example.back.entity.ProductImage;
import com.example.back.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {

    @Value("${productImageLocation}")
    private String productImageLocation;

    private final ProductImageRepository productImageRepository;
    private final FileService fileService;

    public void saveProductImage(ProductImage productImage, MultipartFile itemImgFile) throws Exception {

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {

            imgName = fileService.uploadFile(productImageLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/product/" + imgName;
        }

        //상품 이미지 정보 저장
        productImage.updateProductImg(oriImgName, imgName, imgUrl);
        productImageRepository.save(productImage);
    }

    public void updateProductImage(Long productImageId, MultipartFile itemImgFile) throws Exception {

        if(!itemImgFile.isEmpty()) {

            ProductImage savedProductImage = productImageRepository.findById(productImageId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedProductImage.getImgName())) {

                fileService.deleteFile(productImageLocation + "/" + savedProductImage.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(productImageLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/product/" + imgName;
            savedProductImage.updateProductImg(oriImgName, imgName, imgUrl);
        }
    }

    public void deleteProductImage(Long productImageId) throws Exception {

        ProductImage savedProductImage = productImageRepository.findById(productImageId)
                .orElseThrow(EntityNotFoundException::new);

        //기존 이미지 파일 삭제
        if(!StringUtils.isEmpty(savedProductImage.getImgName())) {

            fileService.deleteFile(productImageLocation + "/" + savedProductImage.getImgName());
        }
    }

}
