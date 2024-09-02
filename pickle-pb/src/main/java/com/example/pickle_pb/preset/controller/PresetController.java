package com.example.pickle_pb.preset.controller;

import com.example.pickle_pb.preset.dto.PresetRequestDto.*;
import com.example.pickle_pb.preset.dto.PresetResponseDto.*;
import com.example.pickle_pb.preset.service.PresetService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pickle-pb/api/preset")
@Validated
@RequiredArgsConstructor
public class PresetController {
    private final PresetService presetService;

    @PostMapping
    public ResponseEntity<CommonResDto<?>> createPreset(@RequestBody CreatePresetRequestDto requestDto) {
        CreatePresetResponseDto result = presetService.createPreset(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "프리셋 생성 완료", result));
    }
}
