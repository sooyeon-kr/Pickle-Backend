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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresetGroupService {
    private final PresetGroupRepository presetGroupRepository;
    private final PbRepository pbRepository;

    public ReadPresetGroupResponseDto readPresetGroup() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundAccountException("PB를 찾을 수 없습니다.");
        }

        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));
        List<PresetGroup> existGroups = presetGroupRepository.findAllByPbId(curPb.getId());

        if (existGroups.isEmpty()) {
            throw new NotFoundGroupException("PB가 가진 그룹이 없습니다.");
        }

        List<ReadDetailPresetGroupResponseDto> nameList = existGroups.stream()
                .map(group -> ReadDetailPresetGroupResponseDto.builder()
                        .presetGroupId(group.getId())
                        .name(group.getName())
                        .build())
                .collect(Collectors.toList());

        return ReadPresetGroupResponseDto.builder()
                .nameList(nameList)
                .build();
    }

    @Transactional
    public CreatePresetGroupResponseDto createPresetGroup(CreatePresetGroupRequestDto createPresetGroupRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundAccountException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));

        PresetGroup presetGroup = PresetGroup.builder()
                .name(createPresetGroupRequestDto.getName())
                .pb(curPb)
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
            throw new NotFoundAccountException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));

        PresetGroup existPresetGroup = presetGroupRepository.findById(updatePresetGroupRequestDto.getPresetGroupId())
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + updatePresetGroupRequestDto.getPresetGroupId()));
        existPresetGroup.setName(updatePresetGroupRequestDto.getName());

        if (curPb != existPresetGroup.getPb()) {
            throw new NotFoundGroupException("로그인한 pb id와 groupid가 다릅니다.");
        }

        PresetGroup result = presetGroupRepository.save(existPresetGroup);

        return UpdatePresetGroupResponseDto.builder()
                .presetGroupId(result.getId())
                .name(result.getName())
                .build();
    }

    @Transactional
    public boolean deletePresetGroup(Integer presetGroupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundAccountException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));

        PresetGroup existPresetGroup = presetGroupRepository.findById(presetGroupId)
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + presetGroupId));

        if (curPb != existPresetGroup.getPb()) {
            throw new NotFoundGroupException("pb의 그룹id가 아닙니다.");
        }

        presetGroupRepository.deleteById(existPresetGroup.getId());

        return true;
    }
}
