package com.example.pickle_customer.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInAccountSaveDTO {
    private int strategyId;
    private int accountId;
    private TradingRequestDTO tradingRequestDTO;
}
