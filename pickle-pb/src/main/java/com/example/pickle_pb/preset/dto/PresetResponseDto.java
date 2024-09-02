package com.example.pickle_pb.preset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PresetResponseDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class CreatePresetResponseDto {
        Integer presetId;
    }
}
