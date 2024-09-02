package com.example.pickle_customer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private int accountId;
    private String accountNumber;
    private int balance;
    private long totalAmount;

}
