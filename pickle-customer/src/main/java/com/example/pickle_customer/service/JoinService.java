package com.example.pickle_customer.service;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.dto.JoinReqDto;
import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.Customer;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.pickle_customer.repository.CustomerRepository;
import com.example.real_common.global.exception.error.DuplicateUserIdException;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JoinService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public void joinProcess(JoinReqDto joinReqDTO) {
        String userId = joinReqDTO.getUserId();

        // 아이디 중복 체크
        if (customerRepository.findByUserId(userId).isPresent()) {
            throw new DuplicateUserIdException("이미 존재하는 아이디입니다: " + userId);
        }

        String password = joinReqDTO.getPassword();
        String username = joinReqDTO.getUsername();
        String email = joinReqDTO.getEmail();
        String phoneNumber = joinReqDTO.getPhoneNumber();

        // MydataId를 찾기 위해 가장 큰 mydataId를 조회
        Integer maxMydataId = customerRepository.findMaxMydataId().orElse(0);

        Customer customer = Customer.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .name(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .mydataId(maxMydataId + 1) // mydataId를 현재 가장 큰 값에서 1 증가시켜 설정
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Account account = Account.builder()
                .accountNumber(generateAccountNumber()) // 자동 생성된 account_number 설정
                .balance(0)
                .totalAmount(0)
                .customer(savedCustomer) // 생성된 CustomerEntity와 연결
                .build();

        accountRepository.save(account);
    }


    private String generateAccountNumber() {
        Random random = new Random();

        int part1 = random.nextInt(1000);        // 000 ~ 999
        int part2 = random.nextInt(1000000);     // 000000 ~ 999999
        int part3 = random.nextInt(100);         // 00 ~ 99
        int part4 = random.nextInt(1000);        // 000 ~ 999

        return String.format("%03d-%06d-%02d-%03d", part1, part2, part3, part4);
    }

    public String generateToken(int customerId) {
        return jwtService.generateToken(customerId);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public Optional<Customer> find(String id){
        return customerRepository.findByUserId(id);
    }
}
