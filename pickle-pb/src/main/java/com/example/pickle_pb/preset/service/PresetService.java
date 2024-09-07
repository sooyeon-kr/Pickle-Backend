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
import com.example.real_common.global.exception.error.NotFoundAccountException;
import com.example.real_common.global.exception.error.NotFoundGroupException;
import com.example.real_common.global.exception.error.NotFoundProductException;
import com.example.real_common.global.exception.error.UnAuthorizedException;
import com.example.real_common.stockEnum.CategoryEnum;
import com.example.real_common.stockEnum.ThemeEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PbRepository pbRepository;

    @Transactional
    public CreatePresetResponseDto createPreset(CreatePresetRequestDto createPresetRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));

        PresetGroup existGroup = presetGroupRepository.findById(createPresetRequestDto.getPresetGroupId())
                .orElseThrow(() -> new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. ID: " + createPresetRequestDto.getPresetGroupId()));

        if (existGroup.getPb() != curPb) {
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
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));
        List<PresetGroup> existGroups = presetGroupRepository.findAllByPbId(curPb.getId());
        if (existGroups.isEmpty()) {
            throw new NotFoundGroupException("프리셋 그룹을 찾을 수 없습니다. PB ID: " + curPb.getId());
        }

        List<ReadPresetListResponseDto.PresetDto> presetList = existGroups.stream()
                .flatMap(group -> presetRepository.findByPresetGroup(group).stream()
                        .map(preset -> {
                            List<ReadPresetListResponseDto.CategoryDto> categoryList = presetCategoryCompositionRepository.findByPreset(preset).stream()
                                    .map(category -> ReadPresetListResponseDto.CategoryDto.builder()
                                            .categoryCompositionId(category.getId())
                                            .categoryName(category.getCategoryName())
                                            .categoryRatio(category.getCategoryRatio())
                                            .build())
                                    .toList();
                            return ReadPresetListResponseDto.PresetDto.builder()
                                    .presetId(preset.getId())
                                    .groupId(group.getId())
                                    .name(preset.getName())
                                    .createdAt(preset.getCreatedAt())
                                    .categoryList(categoryList)
                                    .build();
                        }))
                .toList();
        return ReadPresetListResponseDto.builder()
                .presetList(presetList)
                .build();
    }

    @Transactional
    public UpdatePresetResponseDto updatePreset(Integer presetId, UpdatePresetRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findByPbNumber(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("해당 ID를 가진 PB를 찾을 수 없습니다."));
        Preset existPreset = presetRepository.findById(presetId)
                .orElseThrow(() -> new NotFoundGroupException("프리셋을 찾을 수 없습니다. ID: " + presetId));
        PresetGroup existGroup = presetGroupRepository.findById(existPreset.getPresetGroup().getId())
                .orElseThrow(() -> new NotFoundGroupException("해당하는 그룹이 없습니다."));

        existPreset.updateName(requestDto.getName());
        Preset updatedPreset = presetRepository.save(existPreset);
        // 자산군 구성 비율
        for (UpdatePresetRequestDto.CategoryDto categoryDto : requestDto.getPresetList()) {
            PresetCategoryComposition existCategory;
            if (categoryDto.getCategoryCompositionId() == -1) {
                // 새로운 카테고리 생성
                existCategory = PresetCategoryComposition.builder()
                        .categoryName(categoryDto.getCategoryName())
                        .categoryRatio(categoryDto.getCategoryRatio())
                        .preset(updatedPreset)
                        .build();
            } else {
                existCategory = presetCategoryCompositionRepository.findById(categoryDto.getCategoryCompositionId())
                        .orElseThrow(() -> new NoSuchElementException("PresetCategoryComposition에 해당하는 id가 없음"));
                existCategory.updateCategoryName(categoryDto.getCategoryName());
                existCategory.updateCategoryRatio(categoryDto.getCategoryRatio());
            }
            PresetCategoryComposition savedCategory = presetCategoryCompositionRepository.save(existCategory);
            // 상품 구성 비율
            for (UpdatePresetRequestDto.ProductDto productDto : categoryDto.getProductList()) {
                PresetProductComposition existProduct;
                if (productDto.getProductCompositionId() == -1) {
                    // 새로운 상품 생성
                    existProduct = PresetProductComposition.builder()
                            .code(productDto.getCode())
                            .name(productDto.getName())
                            .themeName(productDto.getThemeName())
                            .ratio(productDto.getRatio())
                            .categoryName(savedCategory.getCategoryName())
                            .categoryComposition(savedCategory)
                            .build();
                } else {
                    existProduct = presetProductCompositionRepository.findById(productDto.getProductCompositionId())
                            .orElseThrow(() -> new NoSuchElementException("PresetProductComposition에 해당하는 id가 없음"));
                    existProduct.updateCode(productDto.getCode());
                    existProduct.updateName(productDto.getName());
                    existProduct.updateRatio(productDto.getRatio());
                }
                presetProductCompositionRepository.save(existProduct);
            }
        }
        List<PresetCategoryComposition> categoryCompositions = presetCategoryCompositionRepository.findByPreset(updatedPreset);
        List<UpdatePresetResponseDto.CategoryDto> categoryDtoList = categoryCompositions.stream()
                .map(categoryComposition -> {
                    List<PresetProductComposition> productCompositions = presetProductCompositionRepository.findByCategoryCompositionId(categoryComposition.getId());
                    List<UpdatePresetResponseDto.ProductDto> productDtoList = productCompositions.stream()
                            .map(productComposition -> (
                                    UpdatePresetResponseDto.ProductDto.builder()
                                            .productCompositionId(productComposition.getId())
                                            .code(productComposition.getCode())
                                            .name(productComposition.getName())
                                            .themeName(productComposition.getThemeName())
                                            .ratio(productComposition.getRatio())
                                            .build()
                            )).toList();
                    return UpdatePresetResponseDto.CategoryDto.builder()
                            .categoryCompositionId(categoryComposition.getId())
                            .categoryName(categoryComposition.getCategoryName())
                            .categoryRatio(categoryComposition.getCategoryRatio())
                            .productList(productDtoList)
                            .build();
                }).toList();

        return UpdatePresetResponseDto.builder()
                .presetId(updatedPreset.getId())
                .groupId(updatedPreset.getPresetGroup().getId())
                .name(updatedPreset.getName())
                .presetList(categoryDtoList)
                .build();
    }

    @Transactional
    public boolean deletePreset(Integer presetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findByPbNumber(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("해당 ID를 가진 PB를 찾을 수 없습니다."));

        Preset existPreset = presetRepository.findById(presetId)
                .orElseThrow(() -> new NotFoundGroupException("프리셋을 찾을 수 없습니다. ID: " + presetId));
        if (!existPreset.getPresetGroup().getPb().equals(curPb)) {
            throw new UnAuthorizedException("사용자의 프리셋이 아닙니다. ");
        }

        presetRepository.deleteById(presetId);

        return true;
    }

    public ReadPresetDetailResponseDto readPresetDetail(Integer presetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));
        Preset existPreset = presetRepository.findById(presetId)
                .orElseThrow(() -> new NotFoundGroupException("프리셋을 찾을 수 없습니다. ID: " + presetId));
        if (!existPreset.getPresetGroup().getPb().equals(curPb)) {
            throw new UnAuthorizedException("사용자의 프리셋이 아닙니다. ");
        }

        List<ReadPresetDetailResponseDto.CategoryDto> categoryDtoListList
                = presetCategoryCompositionRepository.findByPreset(existPreset).stream()
                .map(category -> {
                    List<ReadPresetDetailResponseDto.ProductDto> productDtoList
                            = presetProductCompositionRepository.findByCategoryCompositionId(category.getId()).stream()
                            .map(product -> ReadPresetDetailResponseDto.ProductDto.builder()
                                    .productCompositionId(product.getId())
                                    .code(product.getCode())
                                    .name(product.getName())
                                    .themeName(product.getThemeName())
                                    .ratio(product.getRatio())
                                    .build()).toList();
                    return ReadPresetDetailResponseDto.CategoryDto.builder()
                            .categoryCompositionId(category.getId())
                            .categoryName(category.getCategoryName())
                            .categoryRatio(category.getCategoryRatio())
                            .productList(productDtoList)
                            .build();
                }).toList();
        return ReadPresetDetailResponseDto.builder()
                .presetId(existPreset.getId())
                .groupId(existPreset.getPresetGroup().getId())
                .name(existPreset.getName())
                .presetList(categoryDtoListList)
                .build();
    }

    public ReadPresetListResponseDto readPresetListByGroupId(@Valid Integer presetGroupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UsernameNotFoundException("PB를 찾을 수 없습니다.");
        }
        Pb curPb = pbRepository.findById(Integer.valueOf(authentication.getName()))
                .orElseThrow(() -> new NotFoundAccountException("해당 ID를 가진 PB를 찾을 수 없습니다."));
        PresetGroup group = presetGroupRepository.findByIdAndPbId(presetGroupId, curPb.getId())
                .orElseThrow(() -> new NotFoundGroupException("해당하는 프리셋 그룹을 찾을 수 없습니다. PB ID: " + curPb.getId()));

        List<Preset> presetList = presetRepository.findAllByPresetGroup(group);
        if (presetList.isEmpty()) {
            throw new NotFoundProductException("해당하는 프리셋이 업습니다.");
        }

        List<ReadPresetListResponseDto.PresetDto> presetDtoList = presetList.stream()
                .map(preset -> {
                    List<ReadPresetListResponseDto.CategoryDto> categoryDtoList = presetCategoryCompositionRepository.findByPreset(preset).stream()
                            .map(category -> ReadPresetListResponseDto.CategoryDto.builder()
                                    .categoryCompositionId(category.getId())
                                    .categoryName(category.getCategoryName())
                                    .categoryRatio(category.getCategoryRatio())
                                    .build())
                            .toList();
                    return ReadPresetListResponseDto.PresetDto.builder()
                            .presetId(preset.getId())
                            .groupId(group.getId())
                            .name(preset.getName())
                            .createdAt(preset.getCreatedAt())
                            .categoryList(categoryDtoList)
                            .build();
                })
                .toList();

        return ReadPresetListResponseDto.builder()
                .presetList(presetDtoList)
                .build();
    }
}
