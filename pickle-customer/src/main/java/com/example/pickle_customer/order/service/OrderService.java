package com.example.pickle_customer.order.service;


import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.mystrategy.dto.RestClientDto;
import com.example.pickle_customer.mystrategy.entity.MyStrategy;
import com.example.pickle_customer.mystrategy.entity.MyStrategyCategoryComposition;
import com.example.pickle_customer.mystrategy.entity.MyStrategyProductComposition;
import com.example.pickle_customer.mystrategy.repository.CategoryCompositionRepository;
import com.example.pickle_customer.mystrategy.repository.MyStrategyRepository;
import com.example.pickle_customer.mystrategy.repository.ProductCompositionRepository;
import com.example.pickle_customer.order.dto.*;
import com.example.pickle_customer.order.repository.OrderRepository;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.pickle_customer.repository.ProductRepository;
import com.example.real_common.global.exception.error.NotFoundStrategyException;
import com.example.real_common.global.restClient.CustomRestClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    final private OrderRepository orderRepository;
    final private MyStrategyRepository myStrategyRepository;
    final private AccountRepository accountRepository;
    final private CategoryCompositionRepository categoryCompositionRepository;
    final private ProductCompositionRepository productCompositionRepository;
    private final ProductRepository productRepository;


    public List<HeldQuantityResponseDTO> getQuantity(List<HeldQuantityRequestDTO> requestDTOs) {
        List<HeldQuantityResponseDTO> responseDTOs = new ArrayList<>();

        for (HeldQuantityRequestDTO requestDTO : requestDTOs) {
            ProductInAccount productInAccount = orderRepository.findByProductCode(requestDTO.getProductCode());

            double heldAmount = productInAccount != null
                    ? requestDTO.getHeldQuantity() - productInAccount.getHeldQuantity()
                    : requestDTO.getHeldQuantity();

            responseDTOs.add(HeldQuantityResponseDTO.builder()
                    .productCode(requestDTO.getProductCode())
                    .heldAmount(heldAmount)
                    .build());
        }

        return responseDTOs;
    }


    public List<OrderProductsResDTO> getProducts(Integer strategyId, Integer accountId) {
        List<OrderProductsResDTO> responseList = new ArrayList<>();
        RestClient restClient = CustomRestClient.connectCommon("/inner/strategy");
        RestClientDto.ReadStrategyResponseDto curStrategy = restClient.get()
                .uri("/{strategyId}", strategyId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new NotFoundStrategyException("not found strategy id : " + accountId);
                })
                .body(RestClientDto.ReadStrategyResponseDto.class);


        List<RestClientDto.CategoryDto> categoryCompositions= curStrategy != null ? curStrategy.getCategoryList() : null;
        Account account = accountRepository.findById(accountId).orElse(null);
        MyStrategy myStrategy = myStrategyRepository.findByAccount(account).orElse(null);

        List<MyStrategyCategoryComposition> myCategories;
        if (myStrategy != null) {
            myCategories = categoryCompositionRepository.findAllByMyStrategy(Optional.of(myStrategy));
        } else {
            myCategories = Collections.emptyList();
        }


        assert categoryCompositions != null;
        for (RestClientDto.CategoryDto category : categoryCompositions) {
            List<RestClientDto.ProductDto> products= category.getProductList();

            OrderProductsResDTO categoryRes = OrderProductsResDTO.builder()
                    .categoryName(category.getCategoryName())
                    .categoryRatio(category.getCategoryRatio())
                    .build();
            List<OrderProductsResDTO.ProductDto> productList = new ArrayList<>();
            for (RestClientDto.ProductDto product : products) {
                OrderProductsResDTO.ProductDto.ProductDtoBuilder productDtoBuilder = OrderProductsResDTO.ProductDto.builder()
                        .code(product.getCode())
                        .name(product.getName())
                        .ratio(product.getRatio());
                MyStrategyProductComposition matchedMyProduct = findMatchingMyProduct(myCategories, category, product);
                if (matchedMyProduct != null) {
                    productDtoBuilder
                            .myStrategyRatio(matchedMyProduct.getRatio());
                }else{
                    productDtoBuilder
                            .myStrategyRatio(0.0);
                }
                productList.add(productDtoBuilder.build());
            }

            if(myStrategy!=null){
                List<MyStrategyProductComposition> myProducts = findMyProductsInCategory(myCategories, category);
                for(MyStrategyProductComposition myProduct : myProducts){
                    if(products.stream().noneMatch(p->p.getCode().equals(myProduct.getProductCode()))){
                        OrderProductsResDTO.ProductDto productDTO = OrderProductsResDTO.ProductDto.builder()
                                .code(myProduct.getProductCode())
                                .name(myProduct.getProductName())
                                .ratio(0.0)
                                .myStrategyRatio(myProduct.getRatio())
                                .build();

                        productList.add(productDTO);
                    }
                }
            }
            categoryRes.setProductList(productList);
            responseList.add(categoryRes);
        }
        return responseList;
    }

    private List<MyStrategyProductComposition> findMyProductsInCategory(List<MyStrategyCategoryComposition> myCategories, RestClientDto.CategoryDto category) {
        MyStrategyCategoryComposition matchedCategory = myCategories.stream()
                .filter(myCategory -> myCategory.getCategoryName().equals(category.getCategoryName()))
                .findFirst()
                .orElse(null);

        if (matchedCategory != null) {
            List<MyStrategyProductComposition> allMyProducts = productCompositionRepository.findAllByCategoryComposition(matchedCategory);

            Set<String> existingProductCodes = category.getProductList().stream()
                    .map(RestClientDto.ProductDto::getCode)
                    .collect(Collectors.toSet());

            return allMyProducts.stream()
                    .filter(myProduct -> !existingProductCodes.contains(myProduct.getProductCode()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }


    private MyStrategyProductComposition findMatchingMyProduct(List<MyStrategyCategoryComposition> myCategories, RestClientDto.CategoryDto category, RestClientDto.ProductDto product) {
        for (MyStrategyCategoryComposition myCategory : myCategories) {
            System.out.println(myCategory.getCategoryName());
            if (myCategory.getCategoryName().equals(category.getCategoryName())) {
                List<MyStrategyProductComposition> myProducts = productCompositionRepository.findAllByCategoryComposition(myCategory);
                for (MyStrategyProductComposition myProduct : myProducts) {
                    if (myProduct.getProductCode().equals(product.getCode())) {
                        return myProduct;
                    }
                }
            }
        }
        return null;
    }


    public PriceDTO getPrice(PriceDTO price, int accountId) {
        Account account=accountRepository.findById(accountId).orElse(null);

        assert account != null;
        return PriceDTO.builder()
                .price((int) (price.getPrice()-account.getTotalAmount())).build();


    }

    public PortfolioResDTO getPortfolio(int accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        Optional<MyStrategy> myStrategy = myStrategyRepository.findByAccount(account);
        if (myStrategy.isEmpty()) {
            throw new EntityNotFoundException("MyStrategy not found for the account");
        }
        List<MyStrategyCategoryComposition> categoryCompositions =
                categoryCompositionRepository.findAllByMyStrategy(myStrategy);

        PortfolioResDTO.PortfolioResDTOBuilder portfolioBuilder = PortfolioResDTO.builder()
                .portfolioName(myStrategy.get().getName());

        List<PortfolioResDTO.CategoryDTO> categoryDTOs = new ArrayList<>();
        for (MyStrategyCategoryComposition categoryComposition : categoryCompositions) {
            PortfolioResDTO.CategoryDTO.CategoryDTOBuilder categoryBuilder = PortfolioResDTO.CategoryDTO.builder()
                    .name(categoryComposition.getCategoryName())
                    .ratio(categoryComposition.getCategoryRatio());

            // Calculate total purchase amount for the category
            double totalPurchaseAmount = 0;
            List<MyStrategyProductComposition> productCompositions =
                    productCompositionRepository.findAllByCategoryComposition(categoryComposition);

            List<PortfolioResDTO.ProductDTO> productDTOs = new ArrayList<>();
            for (MyStrategyProductComposition productComposition : productCompositions) {
                ProductInAccount productInAccount =
                        productRepository.findByProductCodeAndAccount(productComposition.getProductCode(), account);

                PortfolioResDTO.ProductDTO.ProductDTOBuilder productBuilder = PortfolioResDTO.ProductDTO.builder()
                        .name(productComposition.getProductName())
                        .code(productComposition.getProductCode())
                        .ratio(productComposition.getRatio())
                        .themeName(productComposition.getThemeName());

                if (productInAccount != null) {
                    productBuilder
                            .quantity(productInAccount.getHeldQuantity())
                            .purchaseAmount(productInAccount.getPurchaseAmount());

                    totalPurchaseAmount += productInAccount.getPurchaseAmount();
                } else {
                    productBuilder
                            .quantity(0)
                            .purchaseAmount(0.0);
                }

                productDTOs.add(productBuilder.build());
            }

            categoryBuilder
                    .totalPurchaseAmount(totalPurchaseAmount)
                    .productDtos(productDTOs);
            categoryDTOs.add(categoryBuilder.build());
        }
        portfolioBuilder.categoryDTOs(categoryDTOs);

        return portfolioBuilder.build();
    }
}


