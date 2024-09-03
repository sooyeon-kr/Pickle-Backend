package com.example.pickle_customer.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private int accountId;
    private String accountNumber;
    private int balance;
    private long totalAmount;

}
