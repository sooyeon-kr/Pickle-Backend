package com.example.pickle_pb.presetGroup.service;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
import com.example.pickle_pb.presetGroup.dto.PresetGroupRequestDto.*;
import com.example.pickle_pb.presetGroup.dto.PresetGroupResponseDto.*;
import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import com.example.pickle_pb.presetGroup.repository.PresetGroupRepository;
import com.example.real_common.global.exception.error.NotFoundAccountException;
import com.example.real_common.global.exception.error.NotFoundGroupException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional
    public CreatePresetGroupResponseDto createPresetGroup(CreatePresetGroupRequestDto createPresetGroupRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        //로그인 구현 후 로직 수정
//        Optional<Pb> curPb = pbRepository.findById(authentication.getPbId());
//        Long curPbId = curPb.get().getId();
//        Pb pb = pbRepository.findById(curPbId)
//                .orElseThrow(() -> new NotFoundAccountException("pb not found with id: " + curPbId));

        PresetGroup presetGroup = PresetGroup.builder()
                .name(createPresetGroupRequestDto.getName())
                .pb(null) //원래는 pb가 들어가야함
                .build();
        PresetGroup result = presetGroupRepository.save(presetGroup);

        return CreatePresetGroupResponseDto.builder()
                .presetGroupId(result.getId())
                .name(result.getName())
                .build();
    }

    @Transactional
    public UpdatePresetGroupResponseDto updatePresetGroup(UpdatePresetGroupRequestDto updatePresetGroupRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
//        Optional<Pb> curPb = pbRepository.findById(authentication.getPbId());
//        Long curPbId = curPb.get().getId();
//        Pb pb = pbRepository.findById(curPbId)
//                .orElseThrow(() -> new NotFoundAccountException("pb not found with id: " + curPbId));

        PresetGroup existPresetGroup = presetGroupRepository.findById((long) updatePresetGroupRequestDto.getPresetGroupId())
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + updatePresetGroupRequestDto.getPresetGroupId()));
        existPresetGroup.setName(updatePresetGroupRequestDto.getName());

        // Todo
        // 로그인한 PB의 id와 그룹 id의 소유 PB가 일치하는 로직 추가

        PresetGroup result = presetGroupRepository.save(existPresetGroup);

        return UpdatePresetGroupResponseDto.builder()
                .presetGroupId(result.getId())
                .name(result.getName())
                .build();
    }

    @Transactional
    public boolean deletePresetGroup(Long presetGroupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        //        Optional<Pb> curPb = pbRepository.findById(authentication.getPbId());
//        Long curPbId = curPb.get().getId();
//        Pb pb = pbRepository.findById(curPbId)
//                .orElseThrow(() -> new NotFoundAccountException("pb not found with id: " + curPbId));
        PresetGroup existPresetGroup = presetGroupRepository.findById(presetGroupId)
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + presetGroupId));

        // Todo
        // 로그인한 PB의 id와 그룹 id의 소유 PB가 일치하는 로직 추가

        presetGroupRepository.deleteById((long) existPresetGroup.getId());

        return true;
    }
}
