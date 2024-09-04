package com.example.pickle_customer.order.service;

import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.order.dto.HeldQuantityRequestDTO;
import com.example.pickle_customer.order.dto.HeldQuantityResponseDTO;
import com.example.pickle_customer.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RebalancingService {
    final private OrderRepository orderRepository;

    public List<HeldQuantityResponseDTO> getQuantity(List<HeldQuantityRequestDTO> requestDTOs) {
        return requestDTOs.stream()
                .flatMap(requestDTO -> {
                    List<ProductInAccount> productInAccounts = orderRepository.findByProductCode(requestDTO.getProductCode());
                    return productInAccounts.stream()
                            .map(productInAccount -> new HeldQuantityResponseDTO(
                                    productInAccount.getProductCode(),
                                     requestDTO.getHeldQuantity()-productInAccount.getHeldQuantity()));
                })
                .collect(Collectors.toList());
    }

}
