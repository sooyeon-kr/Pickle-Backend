package com.example.pickle_customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinReqDto {
    private String userId;
    private String password;
    private String username;
    private String phoneNumber;
    private String email;
}
