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
import com.example.real_common.global.exception.error.IllegalArgumentAmountException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TradingService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final CategoryCompositionRepository myStrategyCategoryCompositionRepository;
    private final ProductCompositionRepository myStrategyProductCompositionRepository;
    private final MyStrategyRepository myStrategyRepository;
    @Transactional
    public void updateTotalAmount(UpdateTotalAmountDTO updateTotalAmountDTO) {
        TradingRequestDTO tradingRequestDTO=updateTotalAmountDTO.getTradingRequestDTO();
        int accountId=updateTotalAmountDTO.getAccountId();
        double tradingAmount = tradingRequestDTO.getTotalAmount();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        double additionalAmountNeeded = tradingAmount - account.getTotalAmount();
        if (additionalAmountNeeded > 0 && account.getBalance() < additionalAmountNeeded){
            throw new IllegalArgumentAmountException("Trading amount is too small");
        }
        Account updateAccount = Account.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance() - additionalAmountNeeded)
                .totalAmount(tradingAmount)
                .customer(account.getCustomer())
                .build();

        accountRepository.save(updateAccount);

    }
    @Transactional
    public void productInAccountSave(ProductInAccountSaveDTO productInAccountSaveDTO) {

        MyStrategy myStrategy = myStrategyRepository.findById(productInAccountSaveDTO.getStrategyId())
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        Account account = accountRepository.findById(productInAccountSaveDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        productRepository.deleteByAccount(account);

        List<MyStrategyCategoryComposition> categories = myStrategyCategoryCompositionRepository.findAllByMyStrategy(myStrategy);
        for (MyStrategyCategoryComposition category : categories) {
            List<MyStrategyProductComposition> products = myStrategyProductCompositionRepository.findAllByCategoryComposition(category);
            for (MyStrategyProductComposition product : products) {
                ProductInAccount newProductInAccount = dtoToProductAccount(product, category, productInAccountSaveDTO.getTradingRequestDTO(), account);
                productRepository.save(newProductInAccount);

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
                .purchaseAmount(matchingProductDTO.getQuantity()*matchingProductDTO.getAmount())
                .build();
    }
}