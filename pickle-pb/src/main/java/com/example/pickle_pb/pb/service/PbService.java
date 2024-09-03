package com.example.pickle_pb.pb.service;

import com.example.pickle_pb.pb.auth.JwtService;
import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.ReadPbResponseDto;
import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.entity.PbPassword;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.real_common.global.exception.error.NotFoundAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PbService {

    private final PbRepository pbRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PbService(PbRepository pbRepository) {
        this.pbRepository = pbRepository;
    }

    public void joinProcess(PbJoinDto pbJoinDTO) {
        String pbnumber = pbJoinDTO.getPbNumber();
        String password = pbJoinDTO.getPassword();
        String office = pbJoinDTO.getBranchOffice();
        String name = pbJoinDTO.getUsername();
        String phone_number = pbJoinDTO.getPhoneNumber();

        Pb data = Pb.builder()
                .pbNumber(pbnumber)
                .password(passwordEncoder.encode(password))
                .branchOffice(office)
                .username(name)
                .phoneNumber(phone_number)
                .build();

        PbPassword data2 = PbPassword.builder()
                .pbNumber(pbnumber)
                .build();

        pbRepository.save(data);
    }

    public String generateToken(String pbnumber) {
        return jwtService.generateToken(pbnumber);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public ReadPbResponseDto.InfoForStrategyDto getPbById(Integer pbId) {
        Pb existPb = pbRepository.findById(Long.valueOf(pbId))
                .orElseThrow(() -> new NotFoundAccountException("not found pb by id : " + pbId));

        return ReadPbResponseDto.InfoForStrategyDto.builder()
                .name(existPb.getUsername())
                .branchOffice(existPb.getBranchOffice())
                .build();
    }
}
