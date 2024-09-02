package com.example.pickle_customer.controller;

import com.example.pickle_customer.dto.AccountResponseDto;
import com.example.pickle_customer.dto.CustomerJoinDto;
import com.example.pickle_customer.dto.CustomerLoginDto;
import com.example.pickle_customer.dto.ProductResponseDto;
import com.example.pickle_customer.service.AccountService;
import com.example.pickle_customer.service.JoinService;
import com.example.pickle_customer.service.ProductService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
public class JoinController {
    @Autowired
    private final JoinService joinService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final ProductService productSevice;

    @Autowired
    public JoinController(JoinService joinService, AuthenticationManager authenticationManager,
                          AccountService accountService, ProductService productService) {
        this.joinService = joinService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.productSevice = productService;
    }

    @PostMapping("/pickle-customer/join")
    public String joinProcess(@RequestBody CustomerJoinDto customerjoinDTO){
        System.out.println("asdasdad");
        joinService.joinProcess(customerjoinDTO);

        return "OK";
    }

    @PostMapping("/pickle-customer/token")
    public String getToken(@RequestBody CustomerLoginDto authRequest) {
        System.out.println("12");
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserid(), authRequest.getPassword()));
        System.out.println(authenticate.getName() + "님 환영합니다");
        if (authenticate.isAuthenticated()) {
            System.out.println("34");
            return joinService.generateToken(authRequest.getUserid());
        } else {
            System.out.println("ab");
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/pickle-customer/validate")
    public String validateToken(@RequestParam("token") String token) {
        joinService.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/pickle-customer/my-asset")
    public AccountResponseDto myAsset(@RequestHeader("Authorization") String token) {
        // "Bearer " 제거 후 토큰 처리

        System.out.println(77);
        String jwtToken = token.substring(7);
        System.out.println(87);
        return accountService.myAsset(jwtToken);
    }

    @GetMapping("/pickle-customer/my-products")
    public List<ProductResponseDto> myProudcts(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return productSevice.myProudcts(jwtToken);
    }

}
