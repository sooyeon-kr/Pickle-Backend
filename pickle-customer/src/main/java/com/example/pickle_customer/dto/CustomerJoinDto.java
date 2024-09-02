package com.example.pickle_customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerJoinDto {


    private String userid;
    private String password;
    private String username;
    private String phonenumber;
    private String email;

}
