//package com.example.pickle_customer.controller;
//
//import com.example.pickle_customer.dto.AccountResponseDto;
//
//import com.example.pickle_customer.service.AccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AccountController {
//
//    @Autowired
//    private final AccountService accountService;
//
//    public AccountController(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//
//
//    @GetMapping("/pickle-customer/my-asset")
//    public AccountResponseDto myAsset(@RequestHeader("Authorization") String token) {
//        // "Bearer " 제거 후 토큰 처리
//        System.out.println(77);
//        String jwtToken = token.substring(7);
//        System.out.println(87);
//        return accountService.myAsset(jwtToken);
//    }
//}
