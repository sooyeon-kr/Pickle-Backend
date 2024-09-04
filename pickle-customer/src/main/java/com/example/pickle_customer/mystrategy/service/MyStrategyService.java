package com.example.pickle_customer.mystrategy.service;

import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.mystrategy.dto.CreateMyStrategyDto;
import com.example.pickle_customer.mystrategy.dto.RestClientDto;
import com.example.pickle_customer.mystrategy.entity.MyStrategyCategoryComposition;
import com.example.pickle_customer.mystrategy.entity.MyStrategy;
import com.example.pickle_customer.mystrategy.entity.MyStrategyProductComposition;
import com.example.pickle_customer.mystrategy.repository.CategoryCompositionRepository;
import com.example.pickle_customer.mystrategy.repository.MyStrategyRepository;
import com.example.pickle_customer.mystrategy.repository.ProductCompositionRepository;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.real_common.global.exception.error.ConflictMyStrategyException;
import com.example.real_common.global.exception.error.NotFoundAccountException;
import com.example.real_common.global.restClient.CustomRestClient;
import com.example.real_common.stockEnum.CategoryEnum;
import com.example.real_common.stockEnum.ThemeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class MyStrategyService {
    private final MyStrategyRepository myStrategyRepository;
    private final CategoryCompositionRepository categoryCompositionRepository;
    private final ProductCompositionRepository productCompositionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CreateMyStrategyDto.Response createMyStrategy(CreateMyStrategyDto.Request request) {
        RestClient restClient = CustomRestClient.connectCommon("/inner/strategy");

        RestClientDto.ReadStrategyResponseDto selectedStrategy = restClient.get()
                .uri("/{strategyId}", request.getSelectedStrategyId())
                .retrieve()
                .body(RestClientDto.ReadStrategyResponseDto.class);

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundAccountException("not found account" + request.getAccountId()));

        MyStrategy checkMyStrategy = myStrategyRepository.findByAccount(account).orElse(null);
        if (checkMyStrategy != null) {
            throw new ConflictMyStrategyException("conflict with exist my strategy");
        }

        MyStrategy myStrategy = MyStrategy.builder()
                .account(account)
                .selectedStrategyId(request.getSelectedStrategyId())
                .name(selectedStrategy.getName())
                .build();

        MyStrategy savedMyStrategy = myStrategyRepository.save(myStrategy);

        for (RestClientDto.CategoryDto categoryDto : selectedStrategy.getCategoryList()) {
            String curCategory = CategoryEnum.checkName(categoryDto.getCategoryName());

            MyStrategyCategoryComposition categoryComposition = MyStrategyCategoryComposition.builder()
                    .myStrategy(savedMyStrategy)
                    .categoryName(curCategory)
                    .categoryRatio(categoryDto.getCategoryRatio())
                    .build();

            MyStrategyCategoryComposition savedCategoryComposition = categoryCompositionRepository.save(categoryComposition);

            for (RestClientDto.ProductDto productDto : categoryDto.getProductList()) {
                String curProductTheme = ThemeEnum.checkName(productDto.getThemeName());

                MyStrategyProductComposition productComposition = MyStrategyProductComposition.builder()
                        .productCode(productDto.getCode())
                        .productName(productDto.getName())
                        .categoryName(curCategory)
                        .ratio(productDto.getRatio())
                        .categoryComposition(savedCategoryComposition)
                        .themeName(curProductTheme)
                        .build();

                productCompositionRepository.save(productComposition);
            }
        }

        return CreateMyStrategyDto.Response.builder()
                .createdMyStrategyId(savedMyStrategy.getId())
                .build();
    }

}
