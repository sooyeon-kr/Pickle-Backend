package com.example.pickle_pb.pb.auth.config;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PbRepository repository;

    @Override
    public UserDetails loadUserByUsername(String pbnumber) throws UsernameNotFoundException {
        log.info("loadUserByPbnumber");
        log.info(pbnumber);

        Pb credential = repository.findByPbNumber(pbnumber)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. username = " + pbnumber));

        // CustomUserDetails 객체를 반환합니다.
        return new CustomUserDetails(credential);
    }



}