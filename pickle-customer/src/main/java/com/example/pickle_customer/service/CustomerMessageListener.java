package com.example.pickle_customer.service;

import com.example.pickle_customer.auth.JwtService;
import com.example.pickle_customer.entity.Customer;
import com.example.pickle_customer.repository.CustomerRepository;
import com.example.real_common.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerMessageListener {

    private static final Logger log = LoggerFactory.getLogger(CustomerMessageListener.class);
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    /**
     * customer토큰을 통해 customerPK를 구하는 메소드
     * @param customerToken
     * @return customer의 PK, 없다면 -1 반환
     */
    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_TOKEN_TO_ID_CONVERSION_QUEUE)
    public Integer handleCustomerTokenRequest(String customerToken) {
        try{
            jwtService.validateToken(customerToken);
            return Integer.parseInt(jwtService.extractUsername(customerToken));
        }catch (RuntimeException e) {
            return RabbitMQConfig.INVALID_TOKEN;
        }
    }

    /**
     * 토큰으로 고객의 정보인 이름와 ID(PK)를 반환하는 메소드
     * @param customerToken
     * @return
     */
    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_TOKEN_TO_NAME_CONVERSION_QUEUE)
    public String handleCustomerInfoRequest(String customerToken) {
        try {
            jwtService.validateToken(customerToken);
            int customerId = Integer.parseInt(jwtService.extractUsername(customerToken));
            System.out.println(customerId+"CU");
            System.out.println(customerRepository.findByCustomerId(customerId).map(Customer::getName).orElseThrow(RuntimeException::new));
            return customerRepository.findByCustomerId(customerId).map(Customer::getName).orElse(RabbitMQConfig.UNKNOWN_CUSTOMER);
        } catch (NumberFormatException e) {
            return RabbitMQConfig.UNKNOWN_CUSTOMER;
        } catch (RuntimeException e) {
            return RabbitMQConfig.UNKNOWN_CUSTOMER;
        }
    }


}
