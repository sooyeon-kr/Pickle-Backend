package com.example.pickle_pb.presetGroup.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PresetGroupRequestDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class CreatePresetGroupRequestDto {
        String name;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class UpdatePresetGroupRequestDto {
        @NotNull(message = "presetGroupId는 필수 값입니다.")
        int presetGroupId;
        String name;
    }
}
