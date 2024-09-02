package com.example.pickle_common.strategy.controller;

import com.example.pickle_common.strategy.dto.CreateStrategyRequestDto;
import com.example.pickle_common.strategy.dto.CreateStrategyResponseDto;
import com.example.pickle_common.strategy.service.StrategyService;
import com.example.real_common.global.common.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/strategy")
@Validated
//@Slf4j
public class StrategyController {
    private final StrategyService strategyService;

    @PostMapping
    public ResponseEntity<CommonResDto<?>> postStrategy(@RequestBody @Valid CreateStrategyRequestDto createStrategyRequestDto) {
        CreateStrategyResponseDto responseDto = strategyService.postStrategy(createStrategyRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "전략 생성 성공", responseDto));
    }
}
