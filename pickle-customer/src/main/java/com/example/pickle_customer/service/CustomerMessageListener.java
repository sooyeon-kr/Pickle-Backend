package com.example.pickle_customer.service;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.repository.CustomerRepository;
import com.example.real_common.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerMessageListener {

    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    /**
     * customer토큰을 통해 customerPK를 구하는 메소드
     * @param customerToken
     * @return customer의 PK, 없다면 -1 반환
     */
    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_TOKEN_TO_ID_CONVERSION_QUEUE)
    public Integer handlePbTokenRequest(String customerToken) {
        try{
            jwtService.validateToken(customerToken);
            return Integer.parseInt(jwtService.extractUsername(customerToken));
        }catch (RuntimeException e) {
            return RabbitMQConfig.INVALID_TOKEN;
        }
    }

}
