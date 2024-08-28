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
public class CustomerJoinDTO {


    private String user_id;
    private String password;
    private String username;
    private String phone_number;
    private String email;

}
