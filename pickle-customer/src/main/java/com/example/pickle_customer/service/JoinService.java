package com.example.pickle_customer.service;

import com.example.pickle_customer.dto.CustomerJoinDTO;
import com.example.pickle_customer.entity.CustomerEntity;
import com.example.pickle_customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final CustomerRepository customerRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public JoinService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public void joinProcess(CustomerJoinDTO customerJoinDTO){
        String user_id = customerJoinDTO.getUser_id();
        String password = customerJoinDTO.getPassword();
        String username = customerJoinDTO.getUsername();
        String email = customerJoinDTO.getEmail();
        String phone_number = customerJoinDTO.getPhone_number();

        CustomerEntity data = new CustomerEntity();
        data.setUser_id(user_id);
        data.setPassword(passwordEncoder.encode(password));
        data.setName(username);
        data.setEmail(email);
        data.setPhone_number(phone_number);
        data.setMydata_id(1);

        customerRepository.save(data);

    }

    public String generateToken(String username) {
        System.out.println("56");
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}