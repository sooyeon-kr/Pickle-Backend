package com.example.pickle_customer.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HeldQuantityResponseDTO {
    private String productCode;
    private double heldAmount;


}
