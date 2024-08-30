package com.example.pickle_pb.presetGroup.dto;

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
        int presetGroupId;
        String name;
    }
}
