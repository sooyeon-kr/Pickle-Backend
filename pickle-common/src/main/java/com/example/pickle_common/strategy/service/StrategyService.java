package com.example.pickle_common.strategy.service;

import com.example.pickle_common.strategy.dto.CreateStrategyRequestDto;
import com.example.pickle_common.strategy.dto.CreateStrategyResponseDto;
import com.example.pickle_common.strategy.dto.ReadStrategyResponseDto;
import com.example.pickle_common.strategy.dto.RestClientDto;
import com.example.pickle_common.strategy.entity.CategoryComposition;
import com.example.pickle_common.strategy.entity.Product;
import com.example.pickle_common.strategy.entity.ProductComposition;
import com.example.pickle_common.strategy.entity.Strategy;
import com.example.pickle_common.strategy.repository.CategoryCompositionRepository;
import com.example.pickle_common.strategy.repository.ProductCompositionRepository;
import com.example.pickle_common.strategy.repository.ProductRepository;
import com.example.pickle_common.strategy.repository.StrategyRepository;
import com.example.real_common.global.exception.error.*;
import com.example.real_common.global.restClient.CustomRestClient;
import com.example.real_common.stockEnum.CategoryEnum;
import com.example.real_common.stockEnum.ThemeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepository;
    private final CategoryCompositionRepository categoryCompositionRepository;
    private final ProductCompositionRepository productCompositionRepository;
    private final ProductRepository productRepository;
//    private final ConsultingHistoryRepository consultingHistoryRepository;


    @Transactional
    public CreateStrategyResponseDto postStrategy(CreateStrategyRequestDto requestDto) {
        //TODO 로그인 한 사용자만 전략을 생성할 수 있다. (PB 혹은 고객)
        // 고객 : 리밸런싱할 때 커스텀 전략 생성 가능 (이 경우 고객id만 들어감)
        // PB : 상담할 때 고객을 위한 전략 생성 (이 경우 고객id, pbId 들어감)

        //TODO 상담 도메인 구현되면 연결하기 (현재는 1로 고정)
//        ConsoultingHistory curConsulting =  consultingHistoryRepository
//                .findbyId(requestDto.getConsultingHistoryId())
//                .orElseThrow(() -> new NotFoundConsultingHistoryException("consulting history not found with id : " + requestDto.getConsultingHistoryId()));
//        int curConsultingId = curConsulting.getId();
        int curConsultingId = 1;

        Strategy strategy = Strategy.builder()
                .pbId(requestDto.getPbId())
                .customerId(requestDto.getCustomerId())
                .name(requestDto.getName())
                .consultingHistoryId(curConsultingId)
                .build();

        Strategy savedStrategy = strategyRepository.save(strategy);

        for (CreateStrategyRequestDto.CategoryDto categoryDto : requestDto.getCategoryList()) {

            String curCategory = CategoryEnum.checkName(categoryDto.getCategory());

            CategoryComposition categoryComposition = CategoryComposition.builder()
                    .strategy(savedStrategy)
                    .categoryName(curCategory)
                    .categoryRatio(categoryDto.getCategoryRatio())
                    .build();

            CategoryComposition createdCategoryComposition = categoryCompositionRepository.save(categoryComposition);

            for (CreateStrategyRequestDto.ProductDto productDTO : categoryDto.getProductList()) {

                String curProductTheme = ThemeEnum.checkName(productDTO.getThemeName());

                Product curProduct = productRepository.findByCode(productDTO.getCode())
                        .orElseThrow(() -> new NotFoundProductException("not found product : " + productDTO.getCode()));

                ProductComposition productComposition = ProductComposition.builder()
                        .name(productDTO.getName())
                        .code(productDTO.getCode())
                        .ratio(productDTO.getRatio())
                        .themeName(curProductTheme)
                        .categoryName(categoryDto.getCategory())
                        .product(curProduct)
                        .categoryComposition(createdCategoryComposition)
                        .build();

                productCompositionRepository.save(productComposition);
            }
        }

        int strategyId = savedStrategy.getStrategyId();

        return new CreateStrategyResponseDto(strategyId);
    }

    public ReadStrategyResponseDto readStrategy() {
        //TODO 로그인한 user가 고객일 경우를 체크, 정보 가져오기
        int customerId = 1; //임시

        List<Strategy> existStrategies = strategyRepository.findAllByCustomerId(customerId);

        RestClient restClient = CustomRestClient.connectPbRestClient("/inner");

        List<ReadStrategyResponseDto.StrategyInfoDto> strategyList = existStrategies.stream()
                .map(existStrategy -> {
                    RestClientDto.PbInfoRequestDto pbInfo = restClient.get()
                            .uri("/{pbId}", existStrategy.getPbId())
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .body(RestClientDto.PbInfoRequestDto.class);

                    List<String> curCategoryComposition = categoryCompositionRepository.findByStrategy_strategyId(existStrategy.getStrategyId()).stream()
                            .map(CategoryComposition::getCategoryName).toList();

                    return ReadStrategyResponseDto.StrategyInfoDto.builder()
                            .name(existStrategy.getName())
                            .pbName(pbInfo.getName())
                            .pbBranchOffice(pbInfo.getBranchOffice())
                            .createdAt(existStrategy.getCreatedAt())
                            .categoryComposition(curCategoryComposition)
                            .build();
                }).toList();


        return ReadStrategyResponseDto.builder()
                .strategyList(strategyList)
                .build();
    }
}

