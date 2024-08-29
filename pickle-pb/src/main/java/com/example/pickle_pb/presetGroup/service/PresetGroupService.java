package com.example.pickle_pb.presetGroup.service;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.pickle_pb.presetGroup.dto.PresetGroupResponseDto.*;
import com.example.pickle_pb.presetGroup.repository.PresetGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PresetGroupService {
    private final PresetGroupRepository presetGroupRepository;
    private final PbRepository pbRepository;

    public ReadPresetGroupResponseDto readPresetGroup() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }

        // 재욱이가 코드 추가하면 오류해결
//        Optional<Pb> curPb = pbRepository.findById(authentication.getPbId());
//        if (curPb.isEmpty()) {
//            throw new UsernameNotFoundException("해당 ID를 가진 PB를 찾을 수 없습니다.");
//        }
//        List<String> names = presetGroupRepository.findAllById(curPb.get().getId());

        return ReadPresetGroupResponseDto.builder()
                .nameList(null) // 일단 null로 하고 pb로그인 구현되면 names로 변경
                .build();
    }
}
