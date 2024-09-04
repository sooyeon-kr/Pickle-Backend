package com.example.pickle_customer.order.service;

import com.example.pickle_customer.dto.ProductResponseDto;
import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.order.dto.ProductDTO;
import com.example.pickle_customer.order.dto.TradingRequestDTO;
import com.example.pickle_customer.order.entity.MyStrategy;
import com.example.pickle_customer.order.entity.MyStrategyCategoryComposition;
import com.example.pickle_customer.order.entity.MyStrategyProductComposition;
import com.example.pickle_customer.order.repository.MyStrategyCategoryCompositionRepository;
import com.example.pickle_customer.order.repository.MyStrategyProductCompositionRepository;
import com.example.pickle_customer.order.repository.MyStrategyRepository;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.pickle_customer.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TradingService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final MyStrategyCategoryCompositionRepository myStrategyCategoryCompositionRepository;
    private final MyStrategyProductCompositionRepository myStrategyProductCompositionRepository;
    private final MyStrategyRepository myStrategyRepository;

    public void updateTotalAmount(TradingRequestDTO tradingRequestDTO) {

        double tradingAmount = tradingRequestDTO.getTotalAmount();
        Account account = accountRepository.findById(4)//로직 따로 빼기
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account updateAccount = Account.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .totalAmount(tradingAmount)
                .customer(account.getCustomer())
                .build();

        accountRepository.save(updateAccount);

    }

    public void productInAccountSave(int strategyId, TradingRequestDTO tradingRequestDTO) {
        MyStrategy myStrategy = myStrategyRepository.findById((long) strategyId)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        Account account = accountRepository.findById(4)//로직 따로 빼기
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<MyStrategyCategoryComposition> categories = myStrategyCategoryCompositionRepository.findByMyStrategy(myStrategy);
        for (MyStrategyCategoryComposition category : categories) {
            List<MyStrategyProductComposition> products = myStrategyProductCompositionRepository.findByMyStrategyCategoryComposition(category);
            for (MyStrategyProductComposition product : products) {
                ProductInAccount productInAccount = dtoToProductAccount(product, category, tradingRequestDTO, account);
                productRepository.save(productInAccount);

            }
        }


    }

    private ProductInAccount dtoToProductAccount(MyStrategyProductComposition product, MyStrategyCategoryComposition category, TradingRequestDTO tradingRequestDTO, Account account) {
        ProductDTO matchingProductDTO = tradingRequestDTO.getProductDTOList().stream()
                .filter(dto -> dto.getProductCode().equals(product.getProductCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Matching ProductDTO not found for product code: " + product.getProductCode()));
        return ProductInAccount.builder()
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .themeName(product.getThemeName())
                .ratioInCategory(category.getCategoryRatio())
                .categoryName(category.getCategoryName())
                .account(account)
                .heldQuantity(matchingProductDTO.getQuantity())
                .heldQuantity(matchingProductDTO.getQuantity()*matchingProductDTO.getAmount())
                .build();
    }
}