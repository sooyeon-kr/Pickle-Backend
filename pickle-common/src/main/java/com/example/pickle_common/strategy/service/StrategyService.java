package com.example.pickle_common.strategy.service;

import com.example.pickle_common.consulting.entity.ConsultingHistory;
import com.example.pickle_common.consulting.repository.ConsultingHistoryRepository;
import com.example.pickle_common.mq.MessageQueueService;
import com.example.pickle_common.strategy.dto.*;
import com.example.pickle_common.strategy.entity.CategoryComposition;
import com.example.pickle_common.strategy.entity.Product;
import com.example.pickle_common.strategy.entity.ProductComposition;
import com.example.pickle_common.strategy.entity.Strategy;
import com.example.pickle_common.strategy.repository.CategoryCompositionRepository;
import com.example.pickle_common.strategy.repository.ProductCompositionRepository;
import com.example.pickle_common.strategy.repository.ProductRepository;
import com.example.pickle_common.strategy.repository.StrategyRepository;
import com.example.pickle_common.userType.UserType;
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
    private final ConsultingHistoryRepository consultingHistoryRepository;
    private final MessageQueueService messageQueueService;

    @Transactional
    public CreateStrategyResponseDto postStrategy(CreateStrategyRequestDto requestDto, String authorizationHeader) {

        int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);
        if (pbId == -1) {
            throw new NotFoundAccountException("not found user account");
        }

        ConsultingHistory curConsultingHistory = consultingHistoryRepository
                .findById(requestDto.getConsultingHistoryId())
                .orElseThrow(()->new NotFoundConsultingHistoryException("consulting history not found with id : " + requestDto.getConsultingHistoryId()));
        int curConsultingId = curConsultingHistory.getId();

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

            CategoryComposition savedCategoryComposition = categoryCompositionRepository.save(categoryComposition);

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
                        .categoryComposition(savedCategoryComposition)
                        .build();

                productCompositionRepository.save(productComposition);
            }
        }

        int strategyId = savedStrategy.getStrategyId();

        return new CreateStrategyResponseDto(strategyId);
    }

    public ReadStrategyResponseDto readStrategy(String authorizationHeader) {
        int customerId = messageQueueService.getCustomerIdByCustomerToken(authorizationHeader);

        List<Strategy> existStrategies = strategyRepository.findAllByCustomerId(customerId);

        RestClient restClient = CustomRestClient.connectPb("/inner");

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

    public ReadDetailStrategyResponseDto pbReadDetailStrategy(Integer strategyId, String authorizationHeader) {
        int pbId = messageQueueService.getPbIdByPbToken(authorizationHeader);

        return readDetailStrategy(strategyId, pbId, UserType.PB); //세부 조회 로직은 현재 동일함
    }

    public ReadDetailStrategyResponseDto cusReadDetailStrategy(Integer strategyId, String authorizationHeader) {
        int customerId = messageQueueService.getCustomerIdByCustomerToken(authorizationHeader);

        return readDetailStrategy(strategyId, customerId, UserType.CUSTOMER);
    }

    public ReadDetailStrategyResponseDto readDetailStrategy(Integer strategyId, int userId, UserType userType) {
        Strategy curStrategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new NotFoundStrategyException("not found strategy Id : " + strategyId));

        if (userType == UserType.PB && curStrategy.getPbId() != userId) {
            throw new UnauthorizedStrategyException("PB cannot access strategy id" + strategyId);
        } else if (userType == UserType.CUSTOMER && curStrategy.getCustomerId() != userId) {
            throw new UnauthorizedStrategyException("customer cannot access strategy id" + strategyId);
        }

        List<ReadDetailStrategyResponseDto.CategoryDto> categoryList = getCategoryDtoList(strategyId);

        return ReadDetailStrategyResponseDto.builder()
                .name(curStrategy.getName())
                .createdAt(curStrategy.getCreatedAt())
                .categoryList(categoryList)
                .build();
    }

    public RestClientDto.ReadStrategyResponseDto getStrategy(int strategyId) {
        Strategy curStrategy = strategyRepository.findById(strategyId).orElseThrow(
                () -> new NotFoundStrategyException("not found strategy id : " + strategyId)
        );

        List<ReadDetailStrategyResponseDto.CategoryDto> categoryDtoList = getCategoryDtoList(strategyId);

        return RestClientDto.ReadStrategyResponseDto.builder()
                .name(curStrategy.getName())
                .strategyId(curStrategy.getStrategyId())
                .categoryList(categoryDtoList)
                .build();
    }

    private List<ReadDetailStrategyResponseDto.CategoryDto> getCategoryDtoList(int strategyId) {
        List<ReadDetailStrategyResponseDto.CategoryDto> categoryList = categoryCompositionRepository.findByStrategy_strategyId(strategyId)
                .stream().map(categoryComposition -> {

                    List<ReadDetailStrategyResponseDto.ProductDto> productList
                            = productCompositionRepository.findAllByCategoryComposition_Id(categoryComposition.getId())
                            .stream().map(productComposition -> {
                                return ReadDetailStrategyResponseDto.ProductDto.builder()
                                        .themeName(productComposition.getThemeName())
                                        .ratio(productComposition.getRatio())
                                        .code(productComposition.getCode())
                                        .name(productComposition.getName())
                                        .build();
                            }).toList();


                    return ReadDetailStrategyResponseDto.CategoryDto.builder()
                            .categoryName(categoryComposition.getCategoryName())
                            .categoryRatio(categoryComposition.getCategoryRatio())
                            .productList(productList)
                            .build();
                }).toList();
        return categoryList;
    }
}

