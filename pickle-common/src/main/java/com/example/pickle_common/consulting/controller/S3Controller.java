package com.example.pickle_common.consulting.controller;

import com.example.pickle_common.consulting.service.S3Service;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/pickle-common/consulting/image")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping()
    public ResponseEntity<CommonResDto<?>> uploadImage(@RequestParam("image") MultipartFile file) {
        // 파일명을 현재 시간 기반으로 생성
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + file.getOriginalFilename();

        String imageUrl = s3Service.saveFile(file, fileName);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "이미지 저장 성공", imageUrl));
    }
}
