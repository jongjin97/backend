package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.CommentDto;
import com.example.back.mybatis.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentMapper commentMapper;

    //댓글 조회
    @GetMapping("/list/{id}")
    public List<CommentDto> listComment(@PathVariable Long id) {
        return commentMapper.selectComment(id);
    }

    //대댓글 조회
    @GetMapping("/list/reply/{id}")
    public  List<CommentDto> listReplyComment(@RequestParam String commentGroup, @PathVariable Long id) {
        return commentMapper.selectReplyComment(commentGroup, id);
    }


    //댓글 추가
    @PostMapping("/new")
    public void postComment(@Valid @RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        commentMapper.createComment(commentDto, principalDetail);
    }

    //대댓글 추가
    @PostMapping("/renew")
    public void postReplyComment(@Valid @RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        System.out.println("commentDto.getCommentGroup() = " + commentDto.getCommentGroup());
        
        commentMapper.createReplyComment(commentDto, principalDetail);
    }

    //댓글 수정
    @PostMapping("/update")
    public void updateComment(@Valid @RequestBody CommentDto commentDto) {
        commentMapper.updateComment(commentDto);
    }

    //댓글 삭제
    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentMapper.deleteComment(id);
    }
}
