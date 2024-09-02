package com.example.real_common.stockEnum;

import com.example.real_common.global.exception.error.NotFoundThemeException;
import lombok.Getter;

@Getter
public enum ThemeEnum {
    // 국내
    TRANSPORTATION_DOMESTIC(1, 1, "운송", "국내"),
    DEFENSE_MATERIAL_DOMESTIC(2, 1, "방위산업물자", "국내"),
    TRAVEL_DOMESTIC(3, 1, "여행", "국내"),
    CLOTHING_DOMESTIC(4, 1, "의류", "국내"),
    ELECTRONIC_COMPONENTS_DOMESTIC(5, 1, "전자부품", "국내"),
    COSMETICS_DOMESTIC(6, 1, "화장품", "국내"),
    HOUSEHOLD_GOODS_DOMESTIC(7, 1, "생활용품", "국내"),
    MEDICAL_DOMESTIC(8, 1, "의료", "국내"),
    BIO_DOMESTIC(9, 1, "바이오", "국내"),
    EDUCATION_DOMESTIC(10, 1, "교육", "국내"),
    PAPER_DOMESTIC(11, 1, "종이", "국내"),
    FOOD_AND_BEVERAGES_DOMESTIC(12, 1, "음식료", "국내"),
    AUTOMOTIVE_DOMESTIC(13, 1, "자동차", "국내"),
    DISPLAY_DOMESTIC(14, 1, "디스플레이", "국내"),
    MACHINERY_DOMESTIC(15, 1, "기계", "국내"),
    SMARTPHONE_DOMESTIC(16, 1, "스마트폰", "국내"),
    TELECOMMUNICATIONS_DOMESTIC(17, 1, "통신", "국내"),
    IT_DOMESTIC(18, 1, "IT", "국내"),
    POWER_ENERGY_DOMESTIC(19, 1, "전력에너지", "국내"),
    FINANCE_DOMESTIC(20, 1, "금융", "국내"),
    DISTRIBUTION_DOMESTIC(21, 1, "유통", "국내"),
    HOME_APPLIANCES_DOMESTIC(22, 1, "가전제품", "국내"),
    GAMING_DOMESTIC(23, 1, "게임", "국내"),
    CHEMISTRY_DOMESTIC(24, 1, "화학", "국내"),
    ENTERTAINMENT_DOMESTIC(25, 1, "엔터테인먼트", "국내"),
    CONSTRUCTION_DOMESTIC(26, 1, "건설", "국내"),
    METAL_DOMESTIC(27, 1, "금속", "국내"),
    LEISURE_DOMESTIC(28, 1, "여가생활", "국내"),
    SEMICONDUCTOR_DOMESTIC(29, 1, "반도체", "국내"),
    HOLDING_COMPANY_DOMESTIC(30, 1, "지주사", "국내"),
    CRUDE_OIL_DOMESTIC(31, 1, "원유", "국내"),
    GAS_ENERGY_DOMESTIC(32, 1, "가스에너지", "국내"),
    SHIPBUILDING_DOMESTIC(33, 1, "조선", "국내"),
    REIT_DOMESTIC(34, 1, "리츠", "국내"),
    WATER_RESOURCES_DOMESTIC(35, 1, "수자원", "국내"),
    AGRICULTURE_DOMESTIC(36, 1, "농업", "국내"),
    BATTERY_DOMESTIC(37, 1, "배터리", "국내"),
    MANAGEMENT_SUPPORT_DOMESTIC(38, 1, "경영지원", "국내"),
    CARBON_REDUCTION_DOMESTIC(39, 1, "탄소저감", "국내"),

    // 해외
    TRANSPORTATION_OVERSEAS(40, 2, "운송", "해외"),
    DEFENSE_MATERIAL_OVERSEAS(41, 2, "방위산업물자", "해외"),
    TRAVEL_OVERSEAS(42, 2, "여행", "해외"),
    CLOTHING_OVERSEAS(43, 2, "의류", "해외"),
    ELECTRONIC_COMPONENTS_OVERSEAS(44, 2, "전자부품", "해외"),
    COSMETICS_OVERSEAS(45, 2, "화장품", "해외"),
    HOUSEHOLD_GOODS_OVERSEAS(46, 2, "생활용품", "해외"),
    MEDICAL_OVERSEAS(47, 2, "의료", "해외"),
    BIO_OVERSEAS(48, 2, "바이오", "해외"),
    EDUCATION_OVERSEAS(49, 2, "교육", "해외"),
    PAPER_OVERSEAS(50, 2, "종이", "해외"),
    FOOD_AND_BEVERAGES_OVERSEAS(51, 2, "음식료", "해외"),
    AUTOMOTIVE_OVERSEAS(52, 2, "자동차", "해외"),
    DISPLAY_OVERSEAS(53, 2, "디스플레이", "해외"),
    MACHINERY_OVERSEAS(54, 2, "기계", "해외"),
    SMARTPHONE_OVERSEAS(55, 2, "스마트폰", "해외"),
    TELECOMMUNICATIONS_OVERSEAS(56, 2, "통신", "해외"),
    IT_OVERSEAS(57, 2, "IT", "해외"),
    POWER_ENERGY_OVERSEAS(58, 2, "전력에너지", "해외"),
    FINANCE_OVERSEAS(59, 2, "금융", "해외"),
    DISTRIBUTION_OVERSEAS(60, 2, "유통", "해외"),
    HOME_APPLIANCES_OVERSEAS(61, 2, "가전제품", "해외"),
    GAMING_OVERSEAS(62, 2, "게임", "해외"),
    CHEMISTRY_OVERSEAS(63, 2, "화학", "해외"),
    ENTERTAINMENT_OVERSEAS(64, 2, "엔터테인먼트", "해외"),
    CONSTRUCTION_OVERSEAS(65, 2, "건설", "해외"),
    METAL_OVERSEAS(66, 2, "금속", "해외"),
    LEISURE_OVERSEAS(67, 2, "여가생활", "해외"),
    SEMICONDUCTOR_OVERSEAS(68, 2, "반도체", "해외"),
    HOLDING_COMPANY_OVERSEAS(69, 2, "지주사", "해외"),
    CRUDE_OIL_OVERSEAS(70, 2, "원유", "해외"),
    GAS_ENERGY_OVERSEAS(71, 2, "가스에너지", "해외"),
    SHIPBUILDING_OVERSEAS(72, 2, "조선", "해외"),
    REIT_OVERSEAS(73, 2, "리츠", "해외"),
    WATER_RESOURCES_OVERSEAS(74, 2, "수자원", "해외"),
    AGRICULTURE_OVERSEAS(75, 2, "농업", "해외"),
    BATTERY_OVERSEAS(76, 2, "배터리", "해외"),
    MANAGEMENT_SUPPORT_OVERSEAS(77, 2, "경영지원", "해외"),
    CARBON_REDUCTION_OVERSEAS(78, 2, "탄소저감", "해외"),

    // 원자재
    METAL_RAW_MATERIAL(79, 4, "금속", "원자재"),
    AGRICULTURAL_PRODUCT(80, 4, "농산물", "원자재"),
    CRUDE_OIL_RAW_MATERIAL(81, 4, "원유", "원자재"),

    // ETF
    DOMESTIC_MARKET_INDEX(82, 5, "국내시장지수", "ETF"),
    DOMESTIC_INDUSTRY(83, 5, "국내업종", "ETF"),
    DOMESTIC_DERIVATIVES(84, 5, "국내파생", "ETF"),
    OVERSEAS_STOCK(85, 5, "해외주식", "ETF");

    private final int id;
    private final int categoryId;
    private final String name;
    private final String categoryName;

    ThemeEnum(int id, int categoryId, String name, String categoryName) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.categoryName = categoryName;
    }

    public static String checkName(String themeName) {
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (themeEnum.getName().equals(themeName)) {
                return themeName;
            }
        }
        throw new NotFoundThemeException("theme not found : " + themeName);
    }
}
