package com.example.pickle_customer.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTotalAmountDTO {
    private TradingRequestDTO tradingRequestDTO;
    private int accountId;

}
