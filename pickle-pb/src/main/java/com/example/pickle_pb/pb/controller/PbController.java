package com.example.pickle_pb.pb.controller;


import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.PbLoginDto;
import com.example.pickle_pb.pb.service.PbJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class PbController {
    @Autowired
    private final PbJoinService pbJoinService;
    @Autowired
    private final AuthenticationManager authenticationManager;


    public PbController(PbJoinService pbJoinService, AuthenticationManager authenticationManager) {
        this.pbJoinService = pbJoinService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/pickle-pb/join")
    public String joinProcess(@RequestBody PbJoinDto pbjoinDTO) {
        System.out.println("asdasdad");
        pbJoinService.joinProcess(pbjoinDTO);

        return "OK";
    }

    @PostMapping("/pickle-pb/token")
    public String getToken(@RequestBody PbLoginDto authRequest) {
        System.out.println("12");
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPbNumber(), authRequest.getPassWord()));
        System.out.println("44");
        if (authenticate.isAuthenticated()) {
            System.out.println("34");
            return pbJoinService.generateToken(authRequest.getPbNumber());
        } else {
            System.out.println("ab");
            throw new RuntimeException("invalid access");

        }
    }

    @GetMapping("/pickle-pb/validate")
    public String validateToken(@RequestParam("token") String token) {
        System.out.println("123");
        pbJoinService.validateToken(token);
        return "Token is valid";
    }
}