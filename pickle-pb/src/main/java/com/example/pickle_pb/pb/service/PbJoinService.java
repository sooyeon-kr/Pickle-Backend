package com.example.pickle_pb.pb.service;

import com.example.pickle_pb.pb.auth.JwtService;
import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.entity.PbPassword;
import com.example.pickle_pb.pb.repository.PbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PbJoinService {

    private final PbRepository pbRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PbJoinService(PbRepository pbRepository) {
        this.pbRepository = pbRepository;
    }

    public void joinProcess(PbJoinDto pbJoinDTO){
        String pbnumber = pbJoinDTO.getPbNumber();
        String password = pbJoinDTO.getPassword();
        String office = pbJoinDTO.getBranchOffice();
        String name = pbJoinDTO.getUsername();
        String phone_number = pbJoinDTO.getPhoneNumber();

        Pb data = new Pb();
        PbPassword data2 = new PbPassword();
        data.setPbNumber(pbnumber);
        data2.setPbNumber(pbnumber);
        data.setPassword(passwordEncoder.encode(password));
        data.setBranchOffice(office);
        data.setName(name);
        data.setPhoneNumber(phone_number);

        pbRepository.save(data);

    }

    public String generateToken(String pbnumber) {
        return jwtService.generateToken(pbnumber);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
