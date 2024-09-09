package com.example.pickle_common.search.service;

import com.example.pickle_common.search.dto.ReadThemeDto;
import com.example.pickle_common.search.entity.Theme;
import com.example.pickle_common.search.repository.ThemeRepository;
import com.example.real_common.stockEnum.CategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ThemeRepository themeRepository;

    public ReadThemeDto.Response readThemeList() {
        List<Theme> themeList = themeRepository.findAll();

        List<ReadThemeDto.CategoryDto> categoryList = new ArrayList<>();

        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            List<String> categoryThemeList = new ArrayList<>();
            categoryList.add(
                    ReadThemeDto.CategoryDto.builder()
                            .category(categoryEnum.getName())
                            .themeList(categoryThemeList)
                            .build()
            );
        }

        for (Theme theme : themeList) {
            String categoryName = CategoryEnum.getNameById(theme.getCategoryId());

            for (ReadThemeDto.CategoryDto categoryDto : categoryList) {
                if (categoryDto.getCategory().equals(categoryName)) {
                    categoryDto.getThemeList().add(theme.getName());
                    break;
                }
            }
        }


        return ReadThemeDto.Response.builder()
                .categories(categoryList)
                .build();
    }
}
