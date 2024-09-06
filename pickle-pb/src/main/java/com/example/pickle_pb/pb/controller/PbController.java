package com.example.pickle_pb.pb.controller;


import com.example.pickle_pb.pb.auth.JwtService;
import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.PbLoginDto;
import com.example.pickle_pb.pb.dto.PbProfileRequestDto;
import com.example.pickle_pb.pb.dto.ReadPbResponseDto;
import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.pickle_pb.pb.service.PbService;
import com.example.real_common.global.common.CommonResDto;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private final PbRepository pbRepository;
    @Autowired
    private final JwtService jwtService;

    public PbController(PbService pbService, AuthenticationManager authenticationManager, PbRepository pbRepository,
                        JwtService jwtService) {
        this.pbService = pbService;
        this.authenticationManager = authenticationManager;
        this.pbRepository = pbRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/pickle-pb/join")
    public ResponseEntity<CommonResDto<?>> joinProcess(@RequestBody PbJoinDto pbjoinDTO) {
        pbService.joinProcess(pbjoinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 회원가입 완료", "환영합니다!"));
    }

    @PostMapping("/api/pickle-pb/token")
    public ResponseEntity<CommonResDto<?>> getToken(@RequestBody PbLoginDto authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPbNumber(), authRequest.getPassword()));

        if (authenticate.isAuthenticated()) {
            Optional<Pb> pbOptional =
                    pbService.find(authRequest.getPbNumber());

            if (pbOptional.isPresent()) {
                Pb pb = pbOptional.get();
                String token = pbService.generateToken(pb.getId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new CommonResDto<>(1, "PB 로그인 및 토큰 발급 완료", token));
             }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResDto<>(-1, "PB 정보를 찾을 수 없습니다", "해당 사용자를 찾을 수 없습니다"));
            }
         }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto<>(-1, "PB 로그인 및 토큰 발급 실패", "잘못된 로그인 정보입니다"));
        }
    }

    @GetMapping("/api/pickle-pb/validate")
    public ResponseEntity<CommonResDto<?>> validateToken(@RequestParam("token") String token) {
        pbService.validateToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "토큰 검증 완료", "토큰 검증완료"));
    }

    @GetMapping("/api/pickle-pb/inner/{pbId}")
    public ResponseEntity<?> getPbById(@PathVariable("pbId") Integer pbId) {
        ReadPbResponseDto.InfoForStrategyDto result = pbService.getPbById(pbId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/api/pickle-pb/profile")
    public ResponseEntity<CommonResDto<?>> postProfile(@RequestBody PbProfileRequestDto pbProfileRequestDto,
                                                       @RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        pbService.postProfile(pbProfileRequestDto, jwtToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 프로필 등록 완료", "PB 프로필 등록 완료"));
    }

    @GetMapping("/api/pickle-pb/pblist")
    public ResponseEntity<CommonResDto<?>> pbList(){
//        String jwtToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 프로필 조회 완료", pbService.pblist()));
    }

    @GetMapping("/api/pickle-pb/pblist/{pbNumber}")
    public ResponseEntity<CommonResDto<?>> pbDetalList(@PathVariable("pbNumber") String pbNumber){
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 개별 프로필 조회 완료", pbService.pblist()));
    }

    @GetMapping("/api/pickle-pb/filterpblist")
    public ResponseEntity<CommonResDto<?>> filterPbList( @RequestParam(name = "mainFields", required = false) String[] mainFields,
                                                         @RequestParam(name = "tags", required = false) String[] tags,
                                                         @RequestParam(name = "minConsultingAmount", required = false) Long minConsultingAmount) {

        // 배열을 리스트로 변환
        List<String> mainFieldList = mainFields != null ? Arrays.asList(mainFields) : null;
        List<String> tagList = tags != null ? Arrays.asList(tags) : null;
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "PB 개별 프로필 조회 완료", pbService.getFilteredPbList(mainFieldList, tagList, minConsultingAmount)));

    }

    @GetMapping("/api/pickle-pb/getpbid")
    public String getPbId(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return jwtService.extractUsername(jwtToken);

    }


}