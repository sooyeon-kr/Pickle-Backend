package com.example.pickle_customer.order.controller;

import com.example.pickle_customer.order.dto.HeldQuantityRequestDTO;
import com.example.pickle_customer.order.dto.HeldQuantityResponseDTO;
import com.example.pickle_customer.order.dto.OrderProductsResDTO;
import com.example.pickle_customer.order.service.OrderService;
import com.example.pickle_customer.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickle-customer/api/trade")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final AccountService accountService;
    @PostMapping(value = "/quantity")
    public List<HeldQuantityResponseDTO> getOrders(@RequestBody List<HeldQuantityRequestDTO> heldQuantityRequestDTOS){
        return orderService.getQuantity(heldQuantityRequestDTOS);
    }
    @GetMapping("/products/{strategyId}")
    public List<OrderProductsResDTO> getProducts(@PathVariable("strategyId") int strategyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
//        AccountResponseDto accountResponseDto = accountService.myAsset(token);
//        int accountId = accountResponseDto.getAccountId();
        return orderService.getProducts(strategyId, 7);
    }

}
