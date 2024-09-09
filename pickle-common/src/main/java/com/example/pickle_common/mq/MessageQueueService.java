package com.example.pickle_common.mq;

import com.example.real_common.config.RabbitMQConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageQueueService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.reply-timeout:10000}") // 10초 (10000밀리초) 기본 타임아웃
    private long replyTimeout;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReplyTimeout(replyTimeout);  // 응답 타임아웃 설정
    }

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
                    RabbitMQConfig.PB_EXCHANGE, RabbitMQConfig.PB_NUMBER_TO_ID_ROUTING_KEY, pbNumber,
                    message -> {
                        message.getMessageProperties().setExpiration("8000");
                        return message;
                    }
            );
            return pbId;
        } catch (AmqpException e) {
            return RabbitMQConfig.INVALID_PB_NUMBER;
        }
    }

    /***
     * authorizationHeader를 보내 pb의 pk를 반환받는 메소드
     * @param authorizationHeader
     * @return pbId
     */
    public int getPbIdByPbToken(String authorizationHeader){
        try {
            String token = authorizationHeader.substring(7);
            Integer pbId = (Integer) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.PB_EXCHANGE, RabbitMQConfig.PB_TOKEN_TO_ID_ROUTING_KEY, token,
                    message -> {
                        message.getMessageProperties().setExpiration("8000");
                        return message;
                    }
            );
            return pbId;
        } catch (Exception e) {
            return RabbitMQConfig.INVALID_TOKEN;
        }
    }

    /***
     * authorizationHeader를 보내 customer의 pk를 반환받는 메소드
     * @param authorizationHeader
     * @return customerId
     */
    public int getCustomerIdByCustomerToken(String authorizationHeader){
        try {
            String token = authorizationHeader.substring(7);
            Integer customerId = (Integer) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.CUSTOMER_EXCHANGE, RabbitMQConfig.CUSTOMER_TOKEN_TO_ID_ROUTING_KEY, token, message -> {
                        message.getMessageProperties().setExpiration("8000");
                        return message;
                    }
            );
            return customerId;
        } catch (Exception e) {
            return RabbitMQConfig.INVALID_TOKEN;
        }
    }
    /***
     * authorizationHeader를 보내 customer의 info를 반환받는 메소드
     * @param authorizationHeader
     * @return customerId
     */
    public String getCustomerNameByCustomerToken(String authorizationHeader){
        try {
            String token = authorizationHeader.substring(7);
            String name = (String) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.CUSTOMER_EXCHANGE, RabbitMQConfig.CUSTOMER_TOKEN_TO_NAME_ROUTING_KEY, token, message -> {
                        message.getMessageProperties().setExpiration("8000");
                        return message;
                    }
            );
            return name;
        } catch (Exception e) {
            return new RabbitMQConfig().UNKNOWN_CUSTOMER;
        }
    }

    /**
     * json형식의 상담룸 정보를 보내는 메소드
     * @param jsonMessage 상담룸 정보를 가진 Message
     */
    public boolean sendMessage(String jsonMessage) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CONSULTING_EXCHANGE,
                    RabbitMQConfig.CONSULTING_ROOM_CREATION_ROUTING_KEY,
                    jsonMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
