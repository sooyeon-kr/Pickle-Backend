package com.example.pickle_common.consulting.controller;

import com.example.pickle_common.consulting.dto.CreateRequestLetterRequestDto;
import com.example.pickle_common.consulting.dto.CreateRequestLetterResponseDto;
import com.example.pickle_common.consulting.service.CustomerConsultingService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pickle-common/consulting/customer")
@Validated
@RequiredArgsConstructor
public class CustomerConsultingController {
    private final CustomerConsultingService customerConsultingService;
    @PostMapping("/request-letters")
    public ResponseEntity<CommonResDto<?>> createRequestLetter(@RequestHeader("Authorization") String authorizationHeader,@RequestBody CreateRequestLetterRequestDto requestDto) {
        CreateRequestLetterResponseDto result = customerConsultingService.createRequestLetter(authorizationHeader, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "요청서 생성 성공", result));
    }
}
