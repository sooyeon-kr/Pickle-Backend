package com.example.pickle_customer.controller;

import com.example.pickle_customer.dto.CustomerJoinDto;
import com.example.pickle_customer.dto.CustomerLoginDto;
import com.example.pickle_customer.service.AccountService;
import com.example.pickle_customer.service.JoinService;
import com.example.pickle_customer.service.ProductService;

import com.example.real_common.global.common.CommonResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class CustomerController {
    @Autowired
    private final JoinService joinService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final ProductService productSevice;

    @Autowired
    public CustomerController(JoinService joinService, AuthenticationManager authenticationManager,
                              AccountService accountService, ProductService productService) {
        this.joinService = joinService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.productSevice = productService;
    }

    @PostMapping("/pickle-customer/join")
    public ResponseEntity<CommonResDto<?>> joinProcess(@RequestBody CustomerJoinDto customerjoinDTO){
        joinService.joinProcess(customerjoinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "고객 회원가입 완료", "환영합니다!"));
    }

    @PostMapping("/pickle-customer/token")
    public ResponseEntity<CommonResDto<?>> getToken(@RequestBody CustomerLoginDto authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserid(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "고객 로그인 및 토큰 발급 완료",joinService.generateToken(authRequest.getUserid())));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(-1, "고객 로그인 및 토큰 발급 실패", "고객 로그인 및 토큰 발급 실패"));
        }
    }

    @GetMapping("/pickle-customer/validate")
    public String validateToken(@RequestParam("token") String token) {
        joinService.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/pickle-customer/my-asset")
    public ResponseEntity<CommonResDto<?>> myAsset(@RequestHeader("Authorization") String token) {
        // "Bearer " 제거 후 토큰 처리
        String jwtToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "내 계좌 및 자산 조회 완료", accountService.myAsset(jwtToken)));
    }

    @GetMapping("/pickle-customer/my-products")
    public ResponseEntity<CommonResDto<?>> myProudcts(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "내 계좌 및 자산 조회 완료", productSevice.myProudcts(jwtToken)));
    }

}