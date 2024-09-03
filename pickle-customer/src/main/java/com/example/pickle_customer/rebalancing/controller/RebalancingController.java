package com.example.pickle_customer.rebalancing.controller;

import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.rebalancing.dto.RebalancingRequestDTO;
import com.example.pickle_customer.rebalancing.dto.RebalancingResponseDTO;
import com.example.pickle_customer.rebalancing.service.RebalancingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickle-customer/api/rebalancing")
@AllArgsConstructor
public class RebalancingController {
    private final RebalancingService rebalancingService;
    @PostMapping(value = "/trade")
    public List<RebalancingResponseDTO> getOrders(@RequestBody List<RebalancingRequestDTO> rebalancingRequestDTOs){
        return rebalancingService.getQuantity(rebalancingRequestDTOs);
    }

}
