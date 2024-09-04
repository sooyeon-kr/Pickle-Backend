package com.example.real_common.stockEnum;

import com.example.real_common.global.exception.error.NotFoundCategoryException;
import lombok.Getter;

@Getter
public enum CategoryEnum {
    KOREA(1, "국내"),
    GLOBAL(2, "해외"),
    BOND(3, "채권"),
    MATERIAL(4, "원자재"),
    ETF(5, "ETF");

    private final int id;
    private final String name;

    CategoryEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String checkName(String categoryName) {
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getName().equals(categoryName)) {
                return categoryEnum.getName();
            }
        }

        throw new NotFoundCategoryException("category not found : " + categoryName);
    }
}