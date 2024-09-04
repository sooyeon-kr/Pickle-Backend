package com.example.pickle_customer.controller;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.dto.CustomerJoinDto;
import com.example.pickle_customer.dto.CustomerLoginDto;
import com.example.pickle_customer.entity.Customer;
import com.example.pickle_customer.repository.CustomerRepository;
import com.example.pickle_customer.service.AccountService;
import com.example.pickle_customer.service.JoinService;
import com.example.pickle_customer.service.ProductService;

import com.example.real_common.global.common.CommonResDto;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
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
    private final CustomerRepository customerRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    public CustomerController(JoinService joinService, AuthenticationManager authenticationManager,
                              AccountService accountService, ProductService productService,
                              CustomerRepository customerRepository, JwtService jwtService) {
        this.joinService = joinService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.productSevice = productService;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/pickle-customer/api/join")
    public ResponseEntity<CommonResDto<?>> joinProcess(@RequestBody CustomerJoinDto customerjoinDTO){
        joinService.joinProcess(customerjoinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "고객 회원가입 완료", "환영합니다!"));
    }

    @PostMapping("/pickle-customer/api/token")
    public ResponseEntity<CommonResDto<?>> getToken(@RequestBody CustomerLoginDto authRequest) {
        // 1. 인증 처리
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserid(), authRequest.getPassword())
        );

        if (authenticate.isAuthenticated()) {
            // 2. userid로 고객 정보 조회
            Optional<Customer> customerOptional = customerRepository.findByUserId(authRequest.getUserid());

            // 3. 고객 정보가 존재하는지 확인
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                // 4. customerid를 기반으로 토큰 생성
                String token = joinService.generateToken(customer.getCustomerId());

                // 5. 성공적으로 토큰 생성 및 반환
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new CommonResDto<>(1, "고객 로그인 및 토큰 발급 완료", token));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResDto<>(-1, "고객 정보를 찾을 수 없습니다", "해당 사용자를 찾을 수 없습니다"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto<>(-1, "고객 로그인 및 토큰 발급 실패", "잘못된 로그인 정보입니다"));
        }
    }


    @GetMapping("/pickle-customer/api/validate")
    public String validateToken(@RequestParam("token") String token) {
        joinService.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/pickle-customer/api/my-asset")
    public ResponseEntity<CommonResDto<?>> myAsset(@RequestHeader("Authorization") String token) {
        // "Bearer " 제거 후 토큰 처리
        String jwtToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "내 계좌 및 자산 조회 완료", accountService.myAsset(jwtToken)));
    }

    @GetMapping("/pickle-customer/api/my-products")
    public ResponseEntity<CommonResDto<?>> myProudcts(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "내 보유 자산 조회 완료", productSevice.myProudcts(jwtToken)));
    }

    @GetMapping("/pickle-customer/api/getcustomerid")
    public String getCustomerId(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return jwtService.extractUsername(jwtToken);

    }

}