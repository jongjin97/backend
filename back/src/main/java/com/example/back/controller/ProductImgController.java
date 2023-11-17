package com.example.back.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/productimg")
public class ProductImgController {

    @GetMapping
    public ResponseEntity<byte[]> downloadFile(@RequestParam(name = "fileName", required = true) String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("images\\" + fileName);
        // url을 통해 image파일 byte[]로 저장
        byte[] imageBytes = classPathResource.getInputStream().readAllBytes();
        System.out.println(imageBytes);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}
