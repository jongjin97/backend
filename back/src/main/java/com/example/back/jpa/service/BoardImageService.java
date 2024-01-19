package com.example.back.jpa.service;

import com.example.back.entity.BoardImage;
import com.example.back.repository.BoardImageRepository;
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
public class BoardImageService {

    @Value("${boardImageLocation}")
    private String boardImageLocation;

    private final BoardImageRepository boardImageRepository;
    private final FileService fileService;

    public void saveBoardImage(BoardImage boardImage, MultipartFile itemImgFile) throws Exception {

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {

            imgName = fileService.uploadFile(boardImageLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/board/" + imgName;
        }

        //상품 이미지 정보 저장
        boardImage.updateBoardImg(oriImgName, imgName, imgUrl);
        boardImageRepository.save(boardImage);
    }

    public void updateBoardImage(Long boardImageId, MultipartFile itemImgFile) throws Exception {

        if(!itemImgFile.isEmpty()) {

            BoardImage savedBoardImage = boardImageRepository.findById(boardImageId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedBoardImage.getImgName())) {

                fileService.deleteFile(boardImageLocation + "/" + savedBoardImage.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(boardImageLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/board/" + imgName;
            savedBoardImage.updateBoardImg(oriImgName, imgName, imgUrl);
        }
    }
}