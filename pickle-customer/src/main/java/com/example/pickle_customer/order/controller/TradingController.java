package com.example.pickle_customer.order.controller;

import com.example.pickle_customer.dto.AccountResponseDto;
import com.example.pickle_customer.mystrategy.dto.CreateMyStrategyDto;
import com.example.pickle_customer.mystrategy.dto.UpdateMyStrategyDto;
import com.example.pickle_customer.mystrategy.service.MyStrategyService;
import com.example.pickle_customer.order.dto.ProductInAccountSaveDTO;
import com.example.pickle_customer.order.dto.TradingRequestDTO;
import com.example.pickle_customer.order.dto.UpdateTotalAmountDTO;
import com.example.pickle_customer.order.service.TradingService;
import com.example.pickle_customer.service.AccountService;
import com.example.real_common.global.exception.error.ConflictMyStrategyException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pickle-customer/trade")
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
public class TradingController {
    private TradingService tradingService;
    private MyStrategyService myStrategyService;
    private AccountService accountService;
    @PostMapping
    public ResponseEntity<Map<String, String>> trading(@RequestBody TradingRequestDTO tradingRequestDTO,@RequestHeader(HttpHeaders.AUTHORIZATION) String token){

            String actualToken = token.replace("Bearer ", "");
            AccountResponseDto accountResponseDto = accountService.myAsset(actualToken);
            int accountId = accountResponseDto.getAccountId();
            CreateMyStrategyDto.Request strategyRequest = CreateMyStrategyDto.Request.builder()
                    .accountId(accountId)
                    .selectedStrategyId(tradingRequestDTO.getStrategyId())
                    .build();

            int strategyId;
                CreateMyStrategyDto.Response response = myStrategyService.createMyStrategy(strategyRequest);

                UpdateMyStrategyDto.Request request = UpdateMyStrategyDto.Request.builder()
                        .accountId(strategyRequest.getAccountId())
                        .selectedStrategyId(tradingRequestDTO.getStrategyId())
                        .build();
                strategyId = myStrategyService.updateMyStrategy(request).getUpdatedStrategyId();


            UpdateTotalAmountDTO updateRequest = UpdateTotalAmountDTO.builder()
                    .accountId(accountId)
                    .tradingRequestDTO(tradingRequestDTO)
                    .build();
            tradingService.updateTotalAmount(updateRequest);

            ProductInAccountSaveDTO productInAccountSaveDTO = ProductInAccountSaveDTO.builder()
                    .accountId(accountId)
                    .strategyId(strategyId)
                    .tradingRequestDTO(tradingRequestDTO)
                    .build();
            tradingService.productInAccountSave(productInAccountSaveDTO);
            Map<String, String> LastResponse = new HashMap<>();
            LastResponse.put("message", "Trading completed successfully");
            return ResponseEntity.ok(LastResponse);


    }
}
