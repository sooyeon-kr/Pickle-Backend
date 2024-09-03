package com.example.pickle_customer.rebalancing.service;

import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.rebalancing.dto.RebalancingRequestDTO;
import com.example.pickle_customer.rebalancing.dto.RebalancingResponseDTO;
import com.example.pickle_customer.rebalancing.repository.RebalancingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RebalancingService {
    final private RebalancingRepository rebalancingRepository;

    public List<RebalancingResponseDTO> getQuantity(List<RebalancingRequestDTO> requestDTOs) {
        return requestDTOs.stream()
                .flatMap(requestDTO -> {
                    List<ProductInAccount> productInAccounts = rebalancingRepository.findByProductCode(requestDTO.getProductCode());
                    return productInAccounts.stream()
                            .map(productInAccount -> new RebalancingResponseDTO(
                                    productInAccount.getProductCode(),
                                    requestDTO.getHeldQuantity()-productInAccount.getHeldQuantity()));
                })
                .collect(Collectors.toList());
    }

}
