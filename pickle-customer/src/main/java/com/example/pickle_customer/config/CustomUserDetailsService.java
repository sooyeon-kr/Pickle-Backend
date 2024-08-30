package com.example.pickle_customer.config;

import com.example.pickle_customer.entity.CustomerEntity;
import com.example.pickle_customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        log.info(username);
        Optional<CustomerEntity> credential = repository.findByName(username);
        return credential.map(customerEntity ->
                        new CustomUserDetails(customerEntity.getUserid(), customerEntity.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }

    // userId로 사용자를 로드하는 메서드
    public UserDetails loadUserByUserId(String userid) throws UsernameNotFoundException {
        log.info("loadUserByUserId");
        log.info("User ID: {}", userid);
        Optional<CustomerEntity> credential = repository.findByUserid(userid);
        return credential.map(customerEntity ->
                        new CustomUserDetails(customerEntity.getUserid(), customerEntity.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userid));
    }
}