package com.example.pickle_pb.preset.service;

import com.example.pickle_pb.preset.dto.PresetRequestDto.*;
import com.example.pickle_pb.preset.dto.PresetResponseDto.*;
import com.example.pickle_pb.preset.entity.Preset;
import com.example.pickle_pb.preset.entity.PresetCategoryComposition;
import com.example.pickle_pb.preset.entity.PresetProductComposition;
import com.example.pickle_pb.preset.repository.PresetCategoryCompositionRepository;
import com.example.pickle_pb.preset.repository.PresetProductCompositionRepository;
import com.example.pickle_pb.preset.repository.PresetRepository;
import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import com.example.pickle_pb.presetGroup.repository.PresetGroupRepository;
import com.example.real_common.global.exception.error.NotFoundGroupException;
import com.example.real_common.stockEnum.CategoryEnum;
import com.example.real_common.stockEnum.ThemeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresetService {
    private final PresetRepository presetRepository;
    private final PresetCategoryCompositionRepository presetCategoryCompositionRepository;
    private final PresetProductCompositionRepository presetProductCompositionRepository;
    private final PresetGroupRepository presetGroupRepository;

    @Transactional
    public CreatePresetResponseDto createPreset(CreatePresetRequestDto createPresetRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        // TODO 재욱이가 코드 추가하면 오류해결
//        Optional<Pb> curPb = pbRepository.findById(authentication.getPbId());
//        if (curPb.isEmpty()) {
//            throw new UsernameNotFoundException("해당 ID를 가진 PB를 찾을 수 없습니다.");
//        }
        PresetGroup existGroup = presetGroupRepository.findById(createPresetRequestDto.getPresetGroupId())
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + createPresetRequestDto.getPresetGroupId()));

        String presetName = createPresetRequestDto.getName();
        List<CreatePresetRequestDto.CategoryDto> presetList = createPresetRequestDto.getPresetList();

        // 프리셋 생성
        Preset preset = Preset.builder()
                .name(presetName)
                .presetGroup(existGroup)
                .build();
        Preset savedPreset = presetRepository.save(preset);

        // 해당 프리셋 카테고리 비율 생성
        for (CreatePresetRequestDto.CategoryDto categoryDto : presetList) {
            String categoryName = checkCategoryName(categoryDto.getCategoryName());
            double categoryRatio = categoryDto.getCategoryRatio();
            PresetCategoryComposition presetCategoryComposition = PresetCategoryComposition.builder()
                    .categoryName(categoryName)
                    .categoryRatio(categoryRatio)
                    .preset(savedPreset)
                    .build();
            presetCategoryCompositionRepository.save(presetCategoryComposition);

            //해당 프리셋 카테고리 상품 구성 비율
            List<CreatePresetRequestDto.ProductDto> productList = categoryDto.getProductList();
            for (CreatePresetRequestDto.ProductDto productDto : productList) {
                String themeName = checkThemeName(productDto.getThemeName());
                PresetProductComposition presetProductComposition = PresetProductComposition.builder()
                        .name(productDto.getName())
                        .code(productDto.getCode())
                        .ratio(productDto.getRatio())
                        .themeName(themeName)
                        .categoryComposition(presetCategoryComposition)
                        .build();
                presetProductCompositionRepository.save(presetProductComposition);
            }

        }

        return CreatePresetResponseDto.builder()
                .presetId(savedPreset.getId())
                .build();
    }

    private String checkThemeName(String themeName) {
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (themeEnum.name().equals(themeName)) {
                return themeName;
            }
        }
        throw new NoSuchElementException("테마가 없습니다.");
    }

    private String checkCategoryName(String categoryName) {
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getName().equals(categoryName)) {
                return categoryName;
            }
        }
        // 모든 enum을 검사했지만 일치하는 이름이 없는 경우 예외를 던짐
        throw new NoSuchElementException("카테고리가 없습니다.");
    }
}
