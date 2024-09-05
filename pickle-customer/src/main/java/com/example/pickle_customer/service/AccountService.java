package com.example.pickle_customer.service;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.dto.AccountResponseDto;
import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.Customer;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.pickle_customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }

    public AccountResponseDto myAsset(String token) {
        // JWT 토큰에서 username 추출
        String userid = jwtService.extractUsername(token);
        System.out.println(userid +"22");
        // username을 통해 CustomerEntity 찾기
        Customer customer = customerRepository.findByCustomerId(Integer.parseInt(userid))
                .orElseThrow(() -> new RuntimeException("User not found"));
        // CustomerId를 통해 Account 찾기
        Account account = accountRepository.findByCustomerCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Account 엔티티를 AccountResponseDto로 변환하여 반환
        return new AccountResponseDto(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getTotalAmount()
        );
    }
}
