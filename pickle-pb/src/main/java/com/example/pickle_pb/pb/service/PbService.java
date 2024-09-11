package com.example.pickle_pb.pb.service;

import com.example.pickle_pb.pb.auth.JwtService;
import com.example.pickle_pb.pb.dto.PbJoinDto;
import com.example.pickle_pb.pb.dto.ReadPbResponseDto;
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
import com.example.real_common.global.exception.error.NotFoundAccountException;
import com.example.pickle_pb.pb.repository.PbTagRepository;
import com.example.pickle_pb.pb.repository.TagRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PbService {

    private final PbRepository pbRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final MainFieldRepository mainFieldRepository;

    private final PbMainFieldRepository pbMainFieldRepository;

    private final TagRepository tagRepository;

    private final PbTagRepository pbTagRepository;

    private final EntityManager entityManager;

    private final S3Service s3Service;

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

    public String generateToken(int id) {
        return jwtService.generateToken(id);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public ReadPbResponseDto.InfoForStrategyDto getPbById(Integer pbId) {
        Pb existPb = pbRepository.findById(pbId)
                .orElseThrow(() -> new NotFoundAccountException("not found pb by id : " + pbId));

        return ReadPbResponseDto.InfoForStrategyDto.builder()
                .name(existPb.getUsername())
                .branchOffice(existPb.getBranchOffice())
                .build();
    }

    @Transactional
    public void postProfile(PbProfileRequestDto pbProfileRequestDto, String token) {

        String pbId = jwtService.extractUsername(token);

        // pbNumber로 Pb 엔티티를 조회
        Pb pb = pbRepository.findById(Integer.valueOf(pbId))
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

    public pbProfileResponseDto getPbDetail(String pbNumber) {
        Pb pb = pbRepository.findByPbNumber(pbNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 PB를 찾을 수 없습니다: " + pbNumber));  // PB 번호로 PB 찾기
        return convertToDto(pb);
    }


    public List<pbProfileResponseDto> getFilteredPbList(List<String> mainFields, List<String> tags, Long minConsultingAmount) {
        List<Pb> filteredPbs = pbRepository.findByFilters(mainFields, tags, minConsultingAmount);
        return filteredPbs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private pbProfileResponseDto convertToDto(Pb pb) {
        String imageUrl = s3Service.getImageUrl(pb.getId() + ".jpg");  // PB 번호에 맞는 이미지 URL 가져오기

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
                .img(imageUrl)  // 이미지 URL 추가
                .build();
    }

    public Optional<Pb> find(String id){
        return pbRepository.findByPbNumber(id);
    }

    public int getPbIdByPbNumber(String pbNumber) {
        Pb existPb = pbRepository.findByPbNumber(pbNumber)
                .orElseThrow(() -> new NotFoundAccountException("not found pb by pbnumber : " + pbNumber));

        return existPb.getId();
    }


}
