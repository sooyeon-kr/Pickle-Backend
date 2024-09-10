package com.example.pickle_pb.pb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PbLoginResponseDto {
    String token;
    int pbId;
    String name;
}
