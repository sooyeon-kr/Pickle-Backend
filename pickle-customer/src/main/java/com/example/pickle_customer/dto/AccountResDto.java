package com.example.pickle_customer.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResDto {

    private int accountId;
    private String accountNumber;
    private double balance;
    private double totalAmount;

}
