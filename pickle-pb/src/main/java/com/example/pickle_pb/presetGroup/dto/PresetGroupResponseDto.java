package com.example.pickle_pb.presetGroup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PresetGroupResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ReadPresetGroupResponseDto {
        List<String> nameList;
    }
}
