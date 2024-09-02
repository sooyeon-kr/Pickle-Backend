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
public class CustomerJoinDto {


    private String userid;
    private String password;
    private String username;
    private String phonenumber;
    private String email;

}
