package com.example.pickle_customer.rebalancing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RebalancingResponseDTO {
    private String productCode;
    private int heldAmount;


}
