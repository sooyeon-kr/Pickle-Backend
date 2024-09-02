package com.example.pickle_pb.preset.service;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.pb.repository.PbRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresetService {
    private final PresetRepository presetRepository;
    private final PresetCategoryCompositionRepository presetCategoryCompositionRepository;
    private final PresetProductCompositionRepository presetProductCompositionRepository;
    private final PresetGroupRepository presetGroupRepository;
    private final PbRepository pbRepository;

    @Transactional
    public CreatePresetResponseDto createPreset(CreatePresetRequestDto createPresetRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findByPbNumber(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("해당 ID를 가진 PB를 찾을 수 없습니다."));

        PresetGroup existGroup = presetGroupRepository.findById(createPresetRequestDto.getPresetGroupId())
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + createPresetRequestDto.getPresetGroupId()));

        if (existGroup.getId() != curPb.getId()) {
            throw new UsernameNotFoundException("pb랑 그룹id 다를때 이거 커스텀에러 추가 예정임");
        }

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
            String categoryName = CategoryEnum.checkName(categoryDto.getCategoryName());
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
                String themeName = ThemeEnum.checkName(productDto.getThemeName());
                PresetProductComposition presetProductComposition = PresetProductComposition.builder()
                        .name(productDto.getName())
                        .code(productDto.getCode())
                        .ratio(productDto.getRatio())
                        .themeName(themeName)
                        .categoryName(categoryName)
                        .categoryComposition(presetCategoryComposition)
                        .build();
                presetProductCompositionRepository.save(presetProductComposition);
            }

        }

        return CreatePresetResponseDto.builder()
                .presetId(savedPreset.getId())
                .build();
    }

    public ReadPresetListResponseDto readPresetList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findByPbNumber(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("해당 ID를 가진 PB를 찾을 수 없습니다."));
        List<PresetGroup> existGroups = presetGroupRepository.findAllByPbId(curPb.getId());
        if (existGroups.isEmpty()) {
            throw new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. PB ID: " + curPb.getId());
        }

        List<ReadPresetListResponseDto.PresetDto> presetList = existGroups.stream()
                .flatMap(group -> presetRepository.findByPresetGroup(group).stream()
                        .map(preset -> {
                            List<ReadPresetListResponseDto.CategoryDto> categoryList = presetCategoryCompositionRepository.findByPreset(preset).stream()
                                    .map(category -> ReadPresetListResponseDto.CategoryDto.builder()
                                            .categoryName(category.getCategoryName())
                                            .categoryRatio(category.getCategoryRatio())
                                            .build())
                                    .toList();
                            return ReadPresetListResponseDto.PresetDto.builder()
                                    .presetId(preset.getId())
                                    .groupId(group.getId())
                                    .name(preset.getName())
                                    .categoryList(categoryList)
                                    .build();
                        }))
                .toList();
        return ReadPresetListResponseDto.builder()
                .presetList(presetList)
                .build();
    }

//    public ReadPresetDetailResponseDto readpresetDetail(String presetId) {
//    }
}
