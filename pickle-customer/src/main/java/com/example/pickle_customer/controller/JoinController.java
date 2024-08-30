package com.example.pickle_customer.controller;

import com.example.pickle_customer.dto.CustomerJoinDTO;
import com.example.pickle_customer.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class JoinController {
    @Autowired
    private final JoinService joinService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JoinController(JoinService joinService, AuthenticationManager authenticationManager) {
        this.joinService = joinService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/pickle-customer/join")
    public String joinProcess(@RequestBody CustomerJoinDTO customerjoinDTO){
        System.out.println("asdasdad");
        joinService.joinProcess(customerjoinDTO);

        return "OK";
    }

    @PostMapping("/pickle-customer/token")
    public String getToken(@RequestBody CustomerJoinDTO authRequest) {
        System.out.println("12");
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        System.out.println("44");
        if (authenticate.isAuthenticated()) {
            System.out.println("34");
            return joinService.generateToken(authRequest.getUsername());
        } else {
            System.out.println("ab");
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/pickle-customer/validate")
    public String validateToken(@RequestParam("token") String token) {
        System.out.println("123");
        joinService.validateToken(token);
        return "Token is valid";
    }



}
