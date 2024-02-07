package com.example.back.jpa.service;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.*;
import com.example.back.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardImageService boardImageService;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public Long createBoard(BoardDto boardDto, List<MultipartFile> boardImgFileList, PrincipalDetail principalDetail) throws Exception {

        User user = userRepository.findById(principalDetail.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + principalDetail.getId()));

        Region region = regionRepository.findByRegionId(principalDetail.getId());

        Board board= Board.builder()
                .bdSubject(boardDto.getBdSubject())
                .bdContents(boardDto.getBdContents())
                .status("Y")
                .user(user)
                .region(region)
                .build();

        boardRepository.save(board);

        //이미지 등록
        for(int i=0; i<boardImgFileList.size(); i++) {

            BoardImage boardImage = new BoardImage();
            boardImage.setBoard(board);
            if(i == 0)
                boardImage.setRepImgYn("Y");
            else
                boardImage.setRepImgYn("N");
            boardImageService.saveBoardImage(boardImage, boardImgFileList.get(i));
        }

        return board.getId();
    }

    @Transactional
    public List<BoardListDto> getBoardById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not exist with ID : " + id));

        List<Board> boardList = boardRepository.findAllByUser(user).orElse(Collections.emptyList());


        return boardList.stream()
                .map(board -> new BoardListDto(board, user))
                .collect(Collectors.toList());


    }


    @Transactional
    public Long updateBoard(@PathVariable Long boardId, BoardDto boardDto, List<MultipartFile> boardImgFileList) throws Exception {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not exist with ID :" + boardId));

        board.updateBoard(boardDto);

        List<Long> boardImgIds = boardImageRepository.countById(boardId);

        //이미지 등록
        if(boardImgIds != null) {
            for (int i = 0; i < boardImgIds.size(); i++) {

                boardImageService.updateBoardImage(boardImgIds.get(i), boardImgFileList.get(i));
            }
        }

        if (boardImgFileList != null && boardImgFileList.size() < boardImgIds.size()) {
            //이미지 등록
            for (int i = 0; i < boardImgFileList.size(); i++) {

                boardImageService.updateBoardImage(boardImgIds.get(i), boardImgFileList.get(i));
            }


            //이미지 삭제
            for (int i = boardImgFileList.size(); i < boardImgIds.size(); i++) {
                boardImageRepository.deleteById(boardImgIds.get(i));
            }
        } else if (boardImgFileList != null) {
            //이미지 등록
            for (int i = 0; i < boardImgIds.size(); i++) {

                boardImageService.updateBoardImage(boardImgIds.get(i), boardImgFileList.get(i));
            }

            for (int i = boardImgIds.size(); i < boardImgFileList.size(); i++) {

                BoardImage boardImage = new BoardImage();
                boardImage.setBoard(board);
                boardImage.setRepImgYn("N");
                boardImageService.saveBoardImage(boardImage, boardImgFileList.get(i));
            }
        }

        return board.getId();
    }

    @Transactional
    public void deleteBoard(Long boardId) throws Exception {

        List<Long> boardImgIds = boardImageRepository.countById(boardId);


        //이미지 등록
        if(boardImgIds != null) {
            for (int i = 0; i < boardImgIds.size(); i++) {

                boardImageService.deleteBoardImage(boardImgIds.get(i));
            }
        }

        boardRepository.deleteById(boardId);
    }

    public List<MainBoardDto> getAllBoard(ProductSearchDto productSearchDto) {

        List<MainBoardDto> boardDtoList = boardRepository.findAllBoardAndImgUrl(productSearchDto);

        return boardDtoList;
    }

    @Transactional(readOnly = true)
    public BoardDetailDto getBoardDetail(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        List<BoardImage> boardImageList = boardImageRepository.findByBoardIdOrderByIdAsc(boardId);
        List<BoardImageDto> boardImageDtoList = new ArrayList<>();

        for(BoardImage boardImage : boardImageList) {

            BoardImageDto boardImageDto = BoardImageDto.of(boardImage);
            boardImageDtoList.add(boardImageDto);
        }

        List<UserInfo> userInfoList = userInfoRepository.findByUser_UserInfo(board.getUser().getUserInfo().getId());
        List<RequestUserInfoDto> userInfoDtoList = new ArrayList<>();

        for(UserInfo userInfo : userInfoList) {
            RequestUserInfoDto userInfoDto = RequestUserInfoDto.of(userInfo);
            userInfoDtoList.add(userInfoDto);
        }

        List<Region> regionList = regionRepository.findByRegion(board.getRegion().getId());
        List<RegionDto> regionDtoList = new ArrayList<>();

        for(Region region : regionList) {
            RegionDto regionDto = RegionDto.of(region);
            regionDtoList.add(regionDto);
        }

        BoardDetailDto boardDetailDto = BoardDetailDto.of(board);
        boardDetailDto.setBoardImageDtoList(boardImageDtoList);
        boardDetailDto.setUserInfoDtoList(userInfoDtoList);
        boardDetailDto.setRegionDtoList(regionDtoList);

        return boardDetailDto;
    }

    @Transactional(readOnly = true)
    public List<MainBoardDto> getBoardUser(PrincipalDetail principalDetail) {

        List<MainBoardDto> boardDtoList = boardRepository.findUserBoardAndImgUrl(principalDetail.getId());

        return boardDtoList;
    }
}