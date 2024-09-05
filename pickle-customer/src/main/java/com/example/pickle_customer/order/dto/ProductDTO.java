package com.example.pickle_customer.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String productCode;
    private double quantity;
    private double amount;

}
