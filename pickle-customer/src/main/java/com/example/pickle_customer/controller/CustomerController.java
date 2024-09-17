package com.example.pickle_customer.controller;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.dto.JoinReqDto;
import com.example.pickle_customer.dto.LoginReqDto;
import com.example.pickle_customer.dto.LoginResDto;
import com.example.pickle_customer.dto.RestClientDto;
import com.example.pickle_customer.entity.Customer;
import com.example.pickle_customer.repository.CustomerRepository;
import com.example.pickle_customer.service.AccountService;
import com.example.pickle_customer.service.JoinService;
import com.example.pickle_customer.service.ProductService;

import com.example.real_common.global.common.CommonResDto;
import com.example.real_common.global.exception.error.DuplicateUserIdException;
import com.example.real_common.global.exception.error.NotFoundAccountException;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final JoinService joinService;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final ProductService productService;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    // 생성자 주입 사용
    @Autowired
    public CustomerController(JoinService joinService, AuthenticationManager authenticationManager,
                              AccountService accountService, ProductService productService,
                              CustomerRepository customerRepository, JwtService jwtService) {
        this.joinService = joinService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.productService = productService;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }

    // 회원가입 처리
    @PostMapping("/api/pickle-customer/join")
    public ResponseEntity<CommonResDto<?>> joinProcess(@RequestBody JoinReqDto joinReqDTO) {
        try {
            joinService.joinProcess(joinReqDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CommonResDto<>(1, "고객 회원가입 완료", "환영합니다!"));
        } catch (DuplicateUserIdException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResDto<>(-1, "회원가입 실패", e.getMessage()));
        }
    }

    // 토큰 발급
    @PostMapping("/api/pickle-customer/token")
    public ResponseEntity<CommonResDto<?>> getToken(@RequestBody LoginReqDto authRequest) {
        // 1. 인증 처리
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserId(), authRequest.getPassword())
            );

            if (authenticate.isAuthenticated()) {
                // 2. userid로 고객 정보 조회
                Optional<Customer> customerOptional = joinService.find(authRequest.getUserId());

                if (customerOptional.isPresent()) {
                    Customer customer = customerOptional.get();

                    // 4. customerid를 기반으로 토큰 생성
                    String token = joinService.generateToken(customer.getCustomerId());

                    // 5. 성공적으로 토큰 생성 및 반환
                    LoginResDto result = LoginResDto.builder()
                            .token(token)
                            .name(customer.getName())
                            .userId(customer.getCustomerId())
                            .build();

                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new CommonResDto<>(1, "고객 로그인 및 토큰 발급 완료", result));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new CommonResDto<>(-1, "고객 정보를 찾을 수 없습니다", "해당 사용자를 찾을 수 없습니다"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResDto<>(-1, "고객 로그인 및 토큰 발급 실패", "잘못된 로그인 정보입니다"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto<>(-1, "인증 실패", "로그인 정보가 잘못되었습니다."));
        }
    }

    // 토큰 검증
    @GetMapping("/api/pickle-customer/validate")
    public String validateToken(@RequestParam("token") String token) {
        try {
            joinService.validateToken(token);
            return "Token is valid";
        } catch (Exception e) {
            return "Invalid Token";
        }
    }

    // 내 자산 조회
    @GetMapping("/api/pickle-customer/my-asset")
    public ResponseEntity<CommonResDto<?>> myAsset(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7); // "Bearer " 제거
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResDto<>(1, "내 계좌 및 자산 조회 완료", accountService.myAsset(jwtToken)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto<>(-1, "토큰 검증 실패", "잘못된 토큰입니다."));
        }
    }

    // 내 상품 조회
    @GetMapping("/api/pickle-customer/my-products")
    public ResponseEntity<CommonResDto<?>> myProducts(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7); // "Bearer " 제거
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResDto<>(1, "내 보유 자산 조회 완료", productService.myProudcts(jwtToken)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto<>(-1, "토큰 검증 실패", "잘못된 토큰입니다."));
        }
    }

    // 고객 ID 조회
    @GetMapping("/api/pickle-customer/getcustomerid")
    public RestClientDto.ReadCusIdResponseDto getCustomerId(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7); // "Bearer " 제거
            return RestClientDto.ReadCusIdResponseDto.builder()
                    .customerId(Integer.parseInt(jwtService.extractUsername(jwtToken)))
                    .build();
        } catch (Exception e) {
            return RestClientDto.ReadCusIdResponseDto.builder().build();
        }
    }

    @GetMapping("/api/pickle-customer/getcustomerName")
    public RestClientDto.ReadCusNameResponseDto getCustomerName(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String customerName = customerRepository.findById(Integer.parseInt(jwtService.extractUsername(jwtToken)))
                    .orElseThrow(()-> new NotFoundAccountException("not found cus"))
                    .getName();

            return RestClientDto.ReadCusNameResponseDto
                    .builder()
                    .customerName(customerName)
                    .build();
        } catch (Exception e) {
            return RestClientDto.ReadCusNameResponseDto
                    .builder().build();
        }
    }
}
