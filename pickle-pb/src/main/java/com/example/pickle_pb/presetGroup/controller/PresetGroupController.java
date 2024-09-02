package com.example.pickle_pb.presetGroup.controller;

import com.example.pickle_pb.presetGroup.dto.PresetGroupRequestDto.*;
import com.example.pickle_pb.presetGroup.service.PresetGroupService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.pickle_pb.presetGroup.dto.PresetGroupResponseDto.*;

@RestController
@RequestMapping("/pickle-pb/api/presetGroup")
@Validated
@RequiredArgsConstructor
public class PresetGroupController {
    private final PresetGroupService presetGroupService;

    @GetMapping
    public ResponseEntity<CommonResDto<?>> readPresetGroup() {
        ReadPresetGroupResponseDto result = presetGroupService.readPresetGroup();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "그룹 조회 성공", result));
    }

    @PostMapping
    public ResponseEntity<CommonResDto<?>> createPresetGroup(CreatePresetGroupRequestDto requestDto) {
        CreatePresetGroupResponseDto result = presetGroupService.createPresetGroup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "그룹 생성 성공", result));
    }

    @PutMapping
    public ResponseEntity<CommonResDto<?>> updatePresetGroup(UpdatePresetGroupRequestDto requestDto) {
        UpdatePresetGroupResponseDto result = presetGroupService.updatePresetGroup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "그룹 수정 성공", result));
    }

    @DeleteMapping
    public ResponseEntity<CommonResDto<?>> deletePresetGroup(@RequestParam("presetGroupId") Integer presetGroupId) {
        boolean result = presetGroupService.deletePresetGroup(presetGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "그룹 삭제 성공", result));
    }
}
