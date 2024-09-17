package com.example.pickle_customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResDto {

    private int accountId;
    private String productName;
    private String productCode;
    private double heldQuantity;
    private double purchaseAmount;
    private String themeName;
    private double ratioInCategory;
    private String categoryName;


    public ProductResDto(int accountId, String productName, String productCode, int heldQuantity, long purchaseAmount, long evaluationAmount, long profitAmount, double profitMargin, String categoryName, String themeName) {
    }
}
