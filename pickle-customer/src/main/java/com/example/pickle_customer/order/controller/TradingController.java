package com.example.pickle_customer.order.controller;

import com.example.pickle_customer.mystrategy.dto.CreateMyStrategyDto;
import com.example.pickle_customer.mystrategy.service.MyStrategyService;
import com.example.pickle_customer.order.dto.ProductInAccountSaveDTO;
import com.example.pickle_customer.order.dto.TradingRequestDTO;
import com.example.pickle_customer.order.dto.UpdateTotalAmountDTO;
import com.example.pickle_customer.order.service.TradingService;
import com.example.pickle_customer.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pickle-customer/trade")
@AllArgsConstructor
public class TradingController {
    private TradingService tradingService;
    private MyStrategyService myStrategyService;
    private AccountService accountService;
    @PostMapping
    public ResponseEntity<String> trading(@RequestBody TradingRequestDTO tradingRequestDTO,@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        try {
//            AccountResponseDto accountResponseDto = accountService.myAsset(token);
//            int accountId = accountResponseDto.getAccountId();
            CreateMyStrategyDto.Request strategyRequest = CreateMyStrategyDto.Request.builder()
                    .accountId(7)
                    .selectedStrategyId(tradingRequestDTO.getStrategyId())
                    .build();
            CreateMyStrategyDto.Response strategyResponse = myStrategyService.createMyStrategy(strategyRequest);
            UpdateTotalAmountDTO updateRequest = UpdateTotalAmountDTO.builder()
                    .accountId(7)
                    .tradingRequestDTO(tradingRequestDTO)
                    .build();
            tradingService.updateTotalAmount(updateRequest);

            ProductInAccountSaveDTO productInAccountSaveDTO = ProductInAccountSaveDTO.builder()
                    .accountId(7)
                    .strategyId(strategyResponse.getCreatedMyStrategyId())
                    .tradingRequestDTO(tradingRequestDTO)
                    .build();
            tradingService.productInAccountSave(productInAccountSaveDTO);
            return ResponseEntity.ok("Trading completed successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error processing trading");
        }
    }
}
