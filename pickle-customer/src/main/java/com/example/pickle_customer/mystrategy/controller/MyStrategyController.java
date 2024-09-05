package com.example.pickle_customer.mystrategy.controller;

import com.example.pickle_customer.mystrategy.dto.CreateMyStrategyDto;
import com.example.pickle_customer.mystrategy.dto.UpdateMyStrategyDto;
import com.example.pickle_customer.mystrategy.service.MyStrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pickle-customer/api/my-strategy")
public class MyStrategyController {
    private final MyStrategyService myStrategyService;

    //테스트용
    @PostMapping
    public CreateMyStrategyDto.Response createMyStrategy(@RequestBody CreateMyStrategyDto.Request request) {
        return myStrategyService.createMyStrategy(request);
    }

    //테스트용
    @PutMapping
    public UpdateMyStrategyDto.Response updateStrategy(@RequestBody UpdateMyStrategyDto.Request request) {
        return myStrategyService.updateMyStrategy(request);
    }
}