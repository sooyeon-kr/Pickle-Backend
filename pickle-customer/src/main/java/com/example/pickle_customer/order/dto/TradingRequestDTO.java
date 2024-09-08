package com.example.pickle_customer.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradingRequestDTO {

    private int strategyId;
    private double totalAmount;
    private List<ProductDTO> productDTOList;
}
