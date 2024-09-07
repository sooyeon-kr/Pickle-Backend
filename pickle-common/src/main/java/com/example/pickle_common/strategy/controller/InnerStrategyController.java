package com.example.pickle_common.strategy.controller;

import com.example.pickle_common.strategy.dto.RestClientDto;
import com.example.pickle_common.strategy.service.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pickle-common/inner/strategy")
public class InnerStrategyController {

    private final StrategyService strategyService;

    @GetMapping("/{strategyId}")
    public ResponseEntity<?> getStrategy(@PathVariable("strategyId") int strategyId) {
        RestClientDto.ReadStrategyResponseDto result = strategyService.getStrategy(strategyId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
