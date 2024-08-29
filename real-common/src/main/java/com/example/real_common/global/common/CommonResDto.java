package com.example.real_common.global.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommonResDto<T> {
    private int code; // 1(성공), -1(실패)
    private String message;
    private T data;

    @Builder
    public CommonResDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
