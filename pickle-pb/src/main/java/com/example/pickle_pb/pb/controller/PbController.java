package com.example.pickle_pb.pb.controller;


import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.PbLoginDto;
import com.example.pickle_pb.pb.service.PbService;
import com.example.real_common.global.common.CommonResDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class PbController {

    @Autowired
    private final PbService pbService;
    @Autowired
    private final AuthenticationManager authenticationManager;


    public PbController(PbService pbService, AuthenticationManager authenticationManager) {
        this.pbService = pbService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/pickle-pb/join")
    public ResponseEntity<CommonResDto<?>> joinProcess(@RequestBody PbJoinDto pbjoinDTO) {
        pbService.joinProcess(pbjoinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 회원가입 완료", "환영합니다!"));
    }

    @PostMapping("/pickle-pb/token")
    public ResponseEntity<CommonResDto<?>> getToken(@RequestBody PbLoginDto authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPbNumber(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 로그인 완료 및 토큰 발급 성공", pbService.generateToken(authRequest.getPbNumber())));

        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(-1, "PB 로그인 실패", "Invalid access"));
        }
    }

    @GetMapping("/pickle-pb/validate")
    public ResponseEntity<CommonResDto<?>> validateToken(@RequestParam("token") String token) {
        pbService.validateToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "토큰 검증 완료", "토큰 검증완료"));
    }
}