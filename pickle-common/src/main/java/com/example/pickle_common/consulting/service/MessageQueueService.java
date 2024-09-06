package com.example.pickle_common.consulting.service;

import com.example.real_common.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageQueueService {
    private final RabbitTemplate rabbitTemplate;
    /**
     * 사번을 MQ로 보내는 메소드
     * @param pbNumber 사번
     */
    public int getPbIdByPbNumberbySync(String pbNumber) {
        try {
            Integer response = (Integer) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.PB_EXCHANGE, RabbitMQConfig.PB_NUMBER_TO_ID_ROUTING_KEY, pbNumber
            );
            if (response == null) {
                return -1;
            }
            return response;
        } catch (Exception e) {
            return -1;
        }
    }

}
