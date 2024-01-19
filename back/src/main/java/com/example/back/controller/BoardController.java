package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.Board;
import com.example.back.jpa.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 게시판 전체 조회
    @GetMapping("/list")
    public List<MainBoardDto> getBoardList(ProductSearchDto productSearchDto) {

        return boardService.getAllBoard(productSearchDto);
    }

    @PostMapping("/new") //board 생성
    public Long createBoard(@RequestPart BoardDto boardDto, @RequestPart List<MultipartFile> boardImgFileList,
                                      @AuthenticationPrincipal PrincipalDetail principalDetail) throws Exception {
        return boardService.createBoard(boardDto, boardImgFileList, principalDetail);
    }

    @GetMapping("/lists") //user에 따른 조회
    public ResponseEntity<List<BoardListDto>> getBoardById(@AuthenticationPrincipal PrincipalDetail principalDetail) {

        List<BoardListDto> boardDtoList = boardService.getBoardById(principalDetail.getId());

        return ResponseEntity.ok(boardDtoList);
    }

    @PutMapping("/lists/{boardId}") //boardId에 따른 board 수정
    public Long updateBoard(@PathVariable Long boardId, @RequestPart BoardDto boardDto, @RequestPart(required = false) List<MultipartFile> boardImgFileList) throws Exception {

        return boardService.updateBoard(boardId, boardDto, boardImgFileList);
    }

    @DeleteMapping("/lists/{boardId}") //board 삭제
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }

}