package com.example.pickle_customer.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeldQuantityResponseDTO {
    private String productCode;
    private int heldAmount;


}
