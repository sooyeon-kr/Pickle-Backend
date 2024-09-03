package com.example.pickle_pb.pb.service;

import com.example.pickle_pb.pb.auth.JwtService;
import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.PbProfileRequestDto;
import com.example.pickle_pb.pb.dto.pbProfileResponseDto;
import com.example.pickle_pb.pb.entity.MainField;
import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.entity.PbMainField;
import com.example.pickle_pb.pb.entity.PbPassword;
import com.example.pickle_pb.pb.entity.PbTag;
import com.example.pickle_pb.pb.entity.Tag;
import com.example.pickle_pb.pb.repository.MainFieldRepository;
import com.example.pickle_pb.pb.repository.PbMainFieldRepository;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.pickle_pb.pb.repository.PbTagRepository;
import com.example.pickle_pb.pb.repository.TagRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PbService {

    private final PbRepository pbRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private MainFieldRepository mainFieldRepository;

    @Autowired
    private PbMainFieldRepository pbMainFieldRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PbTagRepository pbTagRepository;

    @Autowired
    private EntityManager entityManager;

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

    @Transactional
    public void postProfile(PbProfileRequestDto pbProfileRequestDto, String token) {

        String pbNumber = jwtService.extractUsername(token);

        // pbNumber로 Pb 엔티티를 조회
        Pb pb = pbRepository.findByPbNumber(pbNumber)
                .orElseThrow(() -> new RuntimeException("PB not found"));

        System.out.println(entityManager.isOpen());
        Pb mergedpb = entityManager.merge(pb);

        Pb finalPb = pb;
        pbProfileRequestDto.getMainFields().forEach(fieldName -> {
            MainField mainField = MainField.builder()
                    .name(fieldName)
                    .build();
            MainField savedMainField = mainFieldRepository.save(mainField); // 저장된 엔티티 반환

            PbMainField pbMainField = PbMainField.builder()
                    .pb(entityManager.merge(finalPb)) // pb를 영속성 컨텍스트에 병합
                    .mainField(entityManager.merge(savedMainField)) // savedMainField를 병합
                    .build();

            pbMainFieldRepository.save(pbMainField); // 병합된 엔티티 저장
        });

        // Tag 저장
        Pb finalPb1 = pb;
        pbProfileRequestDto.getTags().forEach(tagName -> {
            Tag tag = Tag.builder()
                    .name(tagName)
                    .build();
            tagRepository.save(tag);

            PbTag pbTag = PbTag.builder()
                    .pb(finalPb1)
                    .tag(tag)
                    .build();
            pbTagRepository.save(pbTag);
        });

        pb = Pb.builder()
                .id(pb.getId()) // id는 변경되지 않도록 설정
                .password(pb.getPassword()) // password는 그대로 유지
                .pbNumber(pb.getPbNumber())
                .username(pb.getUsername())
                .phoneNumber(pb.getPhoneNumber())
                .branchOffice(pb.getBranchOffice())
                .email(pbProfileRequestDto.getEmail())
                .introduction(pbProfileRequestDto.getIntroduction())
                .consultingCount(pbProfileRequestDto.getConsultingCount())
                .transactionCount(pbProfileRequestDto.getTransactionCount())
                .minConsultingAmount(pbProfileRequestDto.getMinConsultingAmount())
                .build();

        pbRepository.save(pb);

    }

    public List<pbProfileResponseDto> pblist() {
        List<Pb> pbs = pbRepository.findAll();
        return pbs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private pbProfileResponseDto convertToDto(Pb pb) {
        return pbProfileResponseDto.builder()
                .pbNumber(pb.getPbNumber())
                .username(pb.getUsername())
                .phoneNumber(pb.getPhoneNumber())
                .branchOffice(pb.getBranchOffice())
                .email(pb.getEmail())
                .introduction(pb.getIntroduction())
                .consultingCount(pb.getConsultingCount())
                .transactionCount(pb.getTransactionCount())
                .minConsultingAmount(pb.getMinConsultingAmount())
                .mainFields(pb.getPbMainFields().stream()
                        .map(pbMainField -> pbMainField.getMainField().getName())
                        .collect(Collectors.toList()))
                .tags(pb.getPbTags().stream()
                        .map(pbTag -> pbTag.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
