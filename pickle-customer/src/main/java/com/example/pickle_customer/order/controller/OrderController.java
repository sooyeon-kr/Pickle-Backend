package com.example.pickle_customer.order.controller;

import com.example.pickle_customer.order.dto.HeldQuantityRequestDTO;
import com.example.pickle_customer.order.dto.HeldQuantityResponseDTO;
import com.example.pickle_customer.order.service.RebalancingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickle-customer/api/rebalancing")
@AllArgsConstructor
public class OrderController {
    private final RebalancingService rebalancingService;
    @PostMapping(value = "/trade")
    public List<HeldQuantityResponseDTO> getOrders(@RequestBody List<HeldQuantityRequestDTO> heldQuantityRequestDTOS){
        return rebalancingService.getQuantity(heldQuantityRequestDTOS);
    }

}
