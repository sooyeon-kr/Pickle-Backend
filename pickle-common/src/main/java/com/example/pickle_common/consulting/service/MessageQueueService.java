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
            /**
             * 4초동안 기다림
             */
            Integer pbId = (Integer) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.PB_EXCHANGE, RabbitMQConfig.PB_NUMBER_TO_ID_ROUTING_KEY, pbNumber, message -> {
                        message.getMessageProperties().setExpiration("4000");
                        return message;
                    }
            );
            return pbId;
        } catch (Exception e) {
            return RabbitMQConfig.INVALID_PB_NUMBER;
        }
    }

}
