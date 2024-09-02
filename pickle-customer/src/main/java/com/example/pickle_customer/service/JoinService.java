package com.example.pickle_customer.service;

import com.example.pickle_customer.dto.CustomerJoinDto;
import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.CustomerEntity;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.pickle_customer.repository.CustomerRepository;
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

    public JoinService(CustomerRepository customerRepository, AccountRepository accountRepository){
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public void joinProcess(CustomerJoinDto customerJoinDTO){
        String userid = customerJoinDTO.getUserid();
        String password = customerJoinDTO.getPassword();
        String username = customerJoinDTO.getUsername();
        String email = customerJoinDTO.getEmail();
        String phonenumber = customerJoinDTO.getPhonenumber();

        CustomerEntity data = new CustomerEntity();
        data.setUserId(userid);
        data.setPassword(passwordEncoder.encode(password));
        data.setName(username);
        data.setEmail(email);
        data.setPhoneNumber(phonenumber);
        data.setMydataId(1);

        customerRepository.save(data);
        // Account 생성 및 account_number 설정
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber()); // 자동 생성된 account_number 설정
        account.setBalance(0);
        account.setTotalAmount(0);
        account.setCustomerEntity(data); // 생성된 CustomerEntity와 연결

        // Account 저장
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

    public String generateToken(String username) {
        System.out.println("56");
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}