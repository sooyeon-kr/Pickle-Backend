package com.example.pickle_customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private int accountId;
    private String productName;
    private String productCode;
    private int heldQuantity;
    private long purchaseAmount;
    private long evaluationAmount;
    private long profitAmount;
    private double profitMargin;
    private String themeName;
    private double ratioInCategory;
    private String categoryName;


    public ProductResponseDto(int accountId, String productName, String productCode, int heldQuantity, long purchaseAmount, long evaluationAmount, long profitAmount, double profitMargin, String categoryName, String themeName) {
    }
}
