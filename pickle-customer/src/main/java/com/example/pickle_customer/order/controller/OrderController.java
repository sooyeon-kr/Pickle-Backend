package com.example.pickle_customer.order.controller;

import com.example.pickle_customer.dto.AccountResponseDto;
import com.example.pickle_customer.order.dto.*;
import com.example.pickle_customer.order.service.OrderService;
import com.example.pickle_customer.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/pickle-customer/trade")

@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderService orderService;
    private final AccountService accountService;
    @PostMapping(value = "/quantity")
    public List<HeldQuantityResponseDTO> getOrders(@RequestBody List<HeldQuantityRequestDTO> heldQuantityRequestDTOS){
        return orderService.getQuantity(heldQuantityRequestDTOS);
    }
    @GetMapping("/products/{strategyId}")
    public List<OrderProductsResDTO> getProducts(@PathVariable("strategyId") int strategyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        AccountResponseDto accountResponseDto = accountService.myAsset(token);
        int accountId = accountResponseDto.getAccountId();
        return orderService.getProducts(strategyId, accountId);
    }
    @PostMapping("/price")
    public PriceDTO getPrice(@RequestBody PriceDTO priceDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        AccountResponseDto accountResponseDto = accountService.myAsset(token);
        int accountId = accountResponseDto.getAccountId();
        return orderService.getPrice(priceDTO, accountId);
    }
    @GetMapping("/portfolio")
    public PortfolioResDTO getPortfolio(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        String actualToken = token.replace("Bearer ", "");
        AccountResponseDto accountResponseDto = accountService.myAsset(actualToken);
        int accountId = accountResponseDto.getAccountId();
        return orderService.getPortfolio(accountId);
    }

}