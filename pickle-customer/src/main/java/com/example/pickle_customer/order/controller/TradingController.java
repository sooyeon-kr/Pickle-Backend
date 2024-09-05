package com.example.pickle_customer.order.controller;

import com.example.pickle_customer.mystrategy.dto.CreateMyStrategyDto;
import com.example.pickle_customer.mystrategy.service.MyStrategyService;
import com.example.pickle_customer.order.dto.ProductInAccountSaveDTO;
import com.example.pickle_customer.order.dto.TradingRequestDTO;
import com.example.pickle_customer.order.dto.UpdateTotalAmountDTO;
import com.example.pickle_customer.order.service.TradingService;
import com.example.pickle_customer.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pickle-customer/api/trade")
@AllArgsConstructor
public class TradingController {
    private final ProductRepository productRepository;
    private TradingService tradingService;
    private MyStrategyService myStrategyService;
    @PostMapping(value = "/")
    public  ResponseEntity<String> trading(@RequestBody TradingRequestDTO tradingRequestDTO){
        CreateMyStrategyDto.Request strategyRequest =CreateMyStrategyDto.Request.builder()
                        .accountId(1)
                        .selectedStrategyId(tradingRequestDTO.getStrategyId())
                        .build();
        CreateMyStrategyDto.Response strategyResponse=myStrategyService.createMyStrategy(strategyRequest);
        UpdateTotalAmountDTO updateRequest=UpdateTotalAmountDTO.builder()
                .accountId(1)
                .tradingRequestDTO(tradingRequestDTO)
                .build();
        tradingService.updateTotalAmount(updateRequest);

        ProductInAccountSaveDTO productInAccountSaveDTO=ProductInAccountSaveDTO.builder()
                .strategyId(strategyResponse.getCreatedMyStrategyId())
                .tradingRequestDTO(tradingRequestDTO)
                .build();
        tradingService.productInAccountSave(productInAccountSaveDTO);
        return ResponseEntity.ok("Trading operation completed successfully");
    }


}
