package com.example.pickle_customer.config;

import com.example.pickle_customer.entity.CustomerEntity;
import com.example.pickle_customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
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
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        log.info(userid);
        CustomerEntity credential = repository.findByUserId(userid).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. username = " + userid));
        return new CustomUserDetails(credential);
    }

//    // userid로 사용자를 로드하는 메서드
//    public UserDetails loadUserByUserId(String userid) throws IllegalArgumentException {
//        log.info("loadUserByUserId");
//        log.info("User ID: {}", userid);
//        CustomerEntity credential = repository.findByUserId(userid).orElseThrow(
//                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. userid = " + userid));
//        return new CustomUserDetails(credential);
//    }
}