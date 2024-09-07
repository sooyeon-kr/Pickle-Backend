package com.example.pickle_customer.order.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class HeldQuantityRequestDTO {
    private String productCode;
    private double heldQuantity;


}
