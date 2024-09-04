package com.example.pickle_pb.pb.controller;


import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.PbLoginDto;
import com.example.pickle_pb.pb.dto.PbProfileRequestDto;
import com.example.pickle_pb.pb.dto.ReadPbResponseDto;
import com.example.pickle_pb.pb.service.PbService;
import com.example.real_common.global.common.CommonResDto;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/pickle-pb/api/join")
    public ResponseEntity<CommonResDto<?>> joinProcess(@RequestBody PbJoinDto pbjoinDTO) {
        pbService.joinProcess(pbjoinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 회원가입 완료", "환영합니다!"));
    }

    @PostMapping("/pickle-pb/api/token")
    public ResponseEntity<CommonResDto<?>> getToken(@RequestBody PbLoginDto authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPbNumber(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 로그인 완료 및 토큰 발급 성공", pbService.generateToken(authRequest.getPbNumber())));

        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(-1, "PB 로그인 실패", "Invalid access"));
        }
    }

    @GetMapping("/pickle-pb/api/validate")
    public ResponseEntity<CommonResDto<?>> validateToken(@RequestParam("token") String token) {
        pbService.validateToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "토큰 검증 완료", "토큰 검증완료"));
    }

    @GetMapping("/pickle-pb/inner/{pbId}")
    public ResponseEntity<?> getPbById(@PathVariable("pbId") Integer pbId) {
        ReadPbResponseDto.InfoForStrategyDto result = pbService.getPbById(pbId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/pickle-pb/api/profile")
    public ResponseEntity<CommonResDto<?>> postProfile(@RequestBody PbProfileRequestDto pbProfileRequestDto,
                                                       @RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        pbService.postProfile(pbProfileRequestDto, jwtToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 프로필 등록 완료", "PB 프로필 등록 완료"));
    }

    @GetMapping("/pickle-pb/api/pblist")
    public ResponseEntity<CommonResDto<?>> pbList(){
//        String jwtToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 프로필 조회 완료", pbService.pblist()));
    }

    @GetMapping("/pickle-pb/api/pblist/{pbNumber}")
    public ResponseEntity<CommonResDto<?>> pbDetalList(@PathVariable("pbNumber") String pbNumber){
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 개별 프로필 조회 완료", pbService.pblist()));
    }

    @GetMapping("/pickle-pb/api/filterpblist")
    public ResponseEntity<CommonResDto<?>> filterPbList( @RequestParam(name = "mainFields", required = false) String[] mainFields,
                                                         @RequestParam(name = "tags", required = false) String[] tags,
                                                         @RequestParam(name = "minConsultingAmount", required = false) Long minConsultingAmount) {

        // 배열을 리스트로 변환
        List<String> mainFieldList = mainFields != null ? Arrays.asList(mainFields) : null;
        List<String> tagList = tags != null ? Arrays.asList(tags) : null;
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 개별 프로필 조회 완료", pbService.getFilteredPbList(mainFieldList, tagList, minConsultingAmount)));

    }


}