package com.example.pickle_customer.service;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.dto.ProductResponseDto;
import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.Customer;
import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.repository.AccountRepository;
import com.example.pickle_customer.repository.CustomerRepository;
import com.example.pickle_customer.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    public ProductService(ProductRepository productRepository, AccountRepository accountRepository,
                          CustomerRepository customerRepository, JwtService jwtService) {
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }


    public List<ProductResponseDto> myProudcts(String token) {

        // JWT 토큰에서 username 추출
        String userid = jwtService.extractUsername(token);

        // userid을 통해 CustomerEntity 찾기
        Customer customer = customerRepository.findByUserId(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // CustomerId를 통해 Account 찾기
        Account account = accountRepository.findByCustomerEntityCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // AccountId를 통해 Products 찾기
        List<ProductInAccount> productInAccounts = productRepository.findByAccountAccountId(account.getAccountId());

        // Product 목록을 ProductResponseDto 목록으로 변환
        return productInAccounts.stream()
                .peek(productInAccount -> {
                    System.out.println("Product Name: " + productInAccount.getProductName());
                    System.out.println("Product Code: " + productInAccount.getProductCode());
                })
                .map(productInAccount -> new ProductResponseDto(
                        productInAccount.getAccount().getAccountId(),
                        productInAccount.getProductName(),
                        productInAccount.getProductCode(),
                        productInAccount.getHeldQuantity(),
                        productInAccount.getPurchaseAmount(),
                        productInAccount.getEvaluationAmount(),
                        productInAccount.getProfitAmount(),
                        productInAccount.getProfitMargin(),
                        productInAccount.getThemeName(),
                        productInAccount.getRatioInCategory(),
                        productInAccount.getCategoryName()

                ))
                .collect(Collectors.toList());
    }
}