package com.example.back.jpa.service;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.*;
import com.example.back.repository.BoardImageRepository;
import com.example.back.repository.BoardRepository;
import com.example.back.repository.RegionRepository;
import com.example.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardImageService boardImageService;

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
        if(boardImgFileList != null) {
            for (int i = 0; i < boardImgFileList.size(); i++) {

                boardImageService.updateBoardImage(boardImgIds.get(i), boardImgFileList.get(i));
            }
        }

        return board.getId();
    }

    @Transactional
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not exist with ID :" + boardId));

//        board.getCommentList().remove(0);

        boardRepository.deleteById(boardId);
    }

    public List<MainBoardDto> getAllBoard(ProductSearchDto productSearchDto) {

        List<MainBoardDto> boardDtoList = boardRepository.findAllBoardAndImgUrl(productSearchDto);

        return boardDtoList;
    }
}