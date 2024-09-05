package com.example.pickle_customer.order.service;

import com.example.pickle_customer.entity.ProductInAccount;
import com.example.pickle_customer.order.dto.HeldQuantityRequestDTO;
import com.example.pickle_customer.order.dto.HeldQuantityResponseDTO;
import com.example.pickle_customer.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RebalancingService {
    final private OrderRepository orderRepository;

    public List<HeldQuantityResponseDTO> getQuantity(List<HeldQuantityRequestDTO> requestDTOs) {
        List<HeldQuantityResponseDTO> responseDTOs = new ArrayList<>();

        Set<String> requestProductCodes = requestDTOs.stream()
                .map(HeldQuantityRequestDTO::getProductCode)
                .collect(Collectors.toSet());


        for (HeldQuantityRequestDTO requestDTO : requestDTOs) {
            List<ProductInAccount> productInAccounts = orderRepository.findByProductCode(requestDTO.getProductCode());


            if (productInAccounts.isEmpty()) {
                responseDTOs.add(new HeldQuantityResponseDTO(
                        requestDTO.getProductCode(), requestDTO.getHeldQuantity()));
            } else {
                for (ProductInAccount productInAccount : productInAccounts) {
                    responseDTOs.add(new HeldQuantityResponseDTO(
                            productInAccount.getProductCode(),
                            requestDTO.getHeldQuantity() - productInAccount.getHeldQuantity()));
                }
            }
        }


        List<ProductInAccount> allProductInAccounts = orderRepository.findAll();
        for (ProductInAccount productInAccount : allProductInAccounts) {
            if (!requestProductCodes.contains(productInAccount.getProductCode())) {
                responseDTOs.add(new HeldQuantityResponseDTO(
                        productInAccount.getProductCode(), -productInAccount.getHeldQuantity()));
            }
        }

        return responseDTOs;
    }


}
