package com.example.pickle_customer.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TradingRequestDTO {

    private int strategyId;
    private double totalAmount;
    private List<ProductDTO> productDTOList;
}
