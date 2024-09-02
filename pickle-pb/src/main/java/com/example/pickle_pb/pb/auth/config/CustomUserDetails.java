package com.example.pickle_pb.pb.auth.config;

import com.example.pickle_pb.pb.entity.Pb;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

//    private final String pbNumber;
//    private final String name;
//    private final String password;

    private final Pb pb;

    public CustomUserDetails(Pb pb) {
        this.pb = pb;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
//
//    public String getPbNumber(){
//        return pb.getPbNumber();
//    }

    @Override
    public String getPassword() {
            return pb.getPassword();
    }

    @Override
    public String getUsername() {
            return pb.getPbNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
            return true;
    }

    @Override
    public boolean isAccountNonLocked() {
            return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
            return true;
    }

    @Override
    public boolean isEnabled() {
            return true;
    }
}