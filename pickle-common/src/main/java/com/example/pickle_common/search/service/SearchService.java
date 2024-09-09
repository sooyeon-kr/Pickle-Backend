package com.example.pickle_common.search.service;

import com.example.pickle_common.search.dto.ReadThemeDto;
import com.example.pickle_common.search.dto.SearchProductDto;
import com.example.pickle_common.search.entity.Theme;
import com.example.pickle_common.search.repository.ThemeRepository;
import com.example.pickle_common.search.specification.ProductSpecification;
import com.example.pickle_common.strategy.entity.Product;
import com.example.pickle_common.strategy.repository.ProductRepository;
import com.example.real_common.stockEnum.CategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ThemeRepository themeRepository;
    private final ProductRepository productRepository;

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

    public SearchProductDto.Response searchProduct(String name, String theme, String category) {

        if (category == null) category = "";
        if (theme == null) theme = "";
        if (name == null) name = "";

        Specification<Product> spec;

        if (!category.isEmpty()) {
            if (!theme.isEmpty()) {
                spec = ProductSpecification.containingCategoryAndThemeAndName(category, theme, name);
            } else {
                spec = ProductSpecification.containingCategoryAndName(category, name);
            }
        } else {
            spec = ProductSpecification.containingName(name);
        }

        List<Product> productList = productRepository.findAll(spec);

        List<SearchProductDto.ProductDto> items = productList.stream()
                .map(product -> {
                    return SearchProductDto.ProductDto.builder()
                            .code(product.getCode())
                            .name(product.getName())
                            .imgUrl(product.getImgUrl())
                            .categoryName(product.getCategoryName())
                            .themeName(product.getThemeName())
                            .build();
                }).toList();


        return SearchProductDto.Response.builder()
                .query(name)
                .items(items)
                .build();
    }
}
