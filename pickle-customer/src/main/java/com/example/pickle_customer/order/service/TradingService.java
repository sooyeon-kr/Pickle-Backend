package com.example.pickle_customer.order.service;

import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.mystrategy.entity.MyStrategy;
import com.example.pickle_customer.mystrategy.entity.MyStrategyCategoryComposition;
import com.example.pickle_customer.mystrategy.entity.MyStrategyProductComposition;
import com.example.pickle_customer.mystrategy.repository.CategoryCompositionRepository;
import com.example.pickle_customer.mystrategy.repository.MyStrategyRepository;
import com.example.pickle_customer.mystrategy.repository.ProductCompositionRepository;
import com.example.pickle_customer.order.dto.ProductDTO;
import com.example.pickle_customer.order.dto.ProductInAccountSaveDTO;
import com.example.pickle_customer.order.dto.TradingRequestDTO;
import com.example.pickle_customer.order.dto.UpdateTotalAmountDTO;
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
    private final CategoryCompositionRepository myStrategyCategoryCompositionRepository;
    private final ProductCompositionRepository myStrategyProductCompositionRepository;
    private final MyStrategyRepository myStrategyRepository;

    public void updateTotalAmount(UpdateTotalAmountDTO updateTotalAmountDTO) {
        TradingRequestDTO tradingRequestDTO=updateTotalAmountDTO.getTradingRequestDTO();
        int accountId=updateTotalAmountDTO.getAccountId();
        double tradingAmount = tradingRequestDTO.getTotalAmount();
        Account account = accountRepository.findById(accountId)//로직 따로 빼기
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account updateAccount = Account.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance()-tradingAmount)
                .totalAmount(tradingAmount)
                .customer(account.getCustomer())
                .build();

        accountRepository.save(updateAccount);

    }

    public void productInAccountSave(ProductInAccountSaveDTO productInAccountSaveDTO) {

        MyStrategy myStrategy = myStrategyRepository.findById(productInAccountSaveDTO.getStrategyId())
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        Account account = accountRepository.findById(4)//로직 따로 빼기
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<MyStrategyCategoryComposition> categories = myStrategyCategoryCompositionRepository.findAllByMyStrategy(myStrategy);
        for (MyStrategyCategoryComposition category : categories) {
            List<MyStrategyProductComposition> products = myStrategyProductCompositionRepository.findAllByCategoryComposition(category);
            for (MyStrategyProductComposition product : products) {
                ProductInAccount productInAccount = dtoToProductAccount(product, category, productInAccountSaveDTO.getTradingRequestDTO(), account);
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