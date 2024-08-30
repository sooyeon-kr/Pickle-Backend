package com.example.real_common.stockEnum;

import lombok.Getter;

@Getter
public enum CategoryEnum {
    KOREA(1, "국내"),
    GLOBAL(2, "해외"),
    BOND(3, "채권"),
    MATERIAL(4, "원자재"),
    ETF(5, "채권");

    private final int id;
    private final String name;

    CategoryEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}