package com.example.pickle_customer.mystrategy.service;

import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.mystrategy.dto.CreateMyStrategyDto;
import com.example.pickle_customer.mystrategy.dto.RestClientDto;
import com.example.pickle_customer.mystrategy.dto.UpdateMyStrategyDto;
import com.example.pickle_customer.mystrategy.entity.MyStrategyCategoryComposition;
import com.example.pickle_customer.mystrategy.entity.MyStrategy;
import com.example.pickle_customer.mystrategy.entity.MyStrategyProductComposition;
import com.example.pickle_customer.mystrategy.repository.CategoryCompositionRepository;
import com.example.pickle_customer.mystrategy.repository.MyStrategyRepository;
import com.example.pickle_customer.mystrategy.repository.ProductCompositionRepository;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.real_common.global.exception.error.ConflictMyStrategyException;
import com.example.real_common.global.exception.error.NotFoundAccountException;
import com.example.real_common.global.exception.error.NotFoundMyStrategyException;
import com.example.real_common.global.exception.error.NotFoundStrategyException;
import com.example.real_common.global.restClient.CustomRestClient;
import com.example.real_common.stockEnum.CategoryEnum;
import com.example.real_common.stockEnum.ThemeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new NotFoundStrategyException("not found strategy id : " + request.getSelectedStrategyId());
                })
                .body(RestClientDto.ReadStrategyResponseDto.class);


        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundAccountException("not found account" + request.getAccountId()));

        MyStrategy checkMyStrategy = myStrategyRepository.findByAccount(account).orElse(null);
        if (checkMyStrategy != null) {
            return CreateMyStrategyDto.Response.builder()
                    .createdMyStrategyId(checkMyStrategy.getId())
                    .build();
        }

        MyStrategy myStrategy = MyStrategy.builder()
                .account(account)
                .selectedStrategyId(request.getSelectedStrategyId())
                .name(selectedStrategy.getName())
                .build();

        MyStrategy savedMyStrategy = myStrategyRepository.save(myStrategy);

        saveMyStrategyComposition(selectedStrategy, savedMyStrategy);

        return CreateMyStrategyDto.Response.builder()
                .createdMyStrategyId(savedMyStrategy.getId())
                .build();
    }

    private void saveMyStrategyComposition(RestClientDto.ReadStrategyResponseDto selectedStrategy, MyStrategy savedMyStrategy) {
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
    }

    @Transactional
    public UpdateMyStrategyDto.Response updateMyStrategy(UpdateMyStrategyDto.Request request) {
        RestClient restClient = CustomRestClient.connectCommon("/inner/strategy");

        RestClientDto.ReadStrategyResponseDto selectedStrategy = restClient.get()
                .uri("/{strategyId}", request.getSelectedStrategyId())
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new NotFoundStrategyException("not found strategy id : " + request.getSelectedStrategyId());
                })
                .body(RestClientDto.ReadStrategyResponseDto.class);

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundAccountException("not found account" + request.getAccountId()));

        MyStrategy myStrategy = myStrategyRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundMyStrategyException("not found myStrategy" + request.getSelectedStrategyId()));

        List<MyStrategyCategoryComposition> categoryCompositionList =
                categoryCompositionRepository.findAllByMyStrategy(Optional.ofNullable(myStrategy));

        for (MyStrategyCategoryComposition myStrategyCategoryComposition : categoryCompositionList) {
            productCompositionRepository.deleteAllInBatch(
                    productCompositionRepository.findAllByCategoryComposition(
                            myStrategyCategoryComposition
                    )
            );
        }
        categoryCompositionRepository.deleteAllInBatch(categoryCompositionList);

        myStrategy.updateSelectedStrategyInfo(
                request.getSelectedStrategyId(),
                selectedStrategy.getName()
        );

        myStrategyRepository.save(myStrategy);

        saveMyStrategyComposition(selectedStrategy, myStrategy);

        return UpdateMyStrategyDto.Response.builder()
                .updatedStrategyId(myStrategy.getId())
                .build();
    }
}
