package com.example.pickle_common.strategy.controller;

import com.example.pickle_common.strategy.dto.RestClientDto;
import com.example.pickle_common.strategy.service.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pickle-common/inner/strategy")
public class InnerStrategyController {

    private final StrategyService strategyService;

    @GetMapping("/{strategyId}")
    public ResponseEntity<?> getStrategy(@PathVariable int strategyId) {
        RestClientDto.ReadStrategyResponseDto result = strategyService.getStrategy(strategyId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
