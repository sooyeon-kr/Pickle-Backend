package com.example.real_common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Exchange names
    public static final String PB_EXCHANGE = "pb.direct.exchange";
    public static final String COMMON_EXCHANGE = "common.direct.exchange";
    public static final String CUSTOMER_EXCHANGE = "customer.direct.exchange";

    // Routing keys
    public static final String PB_NUMBER_TO_ID_ROUTING_KEY = "pbRoutingKey.pbNumber";

    // Queue names
    public static final String PB_NUMBER_TO_ID_CONVERSION_QUEUE = "pbIdQueue";
    public static final String DEAD_LETTER_QUEUE_NAME = "deadLetterQueue";

    // Exchanges
    @Bean
    public DirectExchange pbExchange() {
        return new DirectExchange(PB_EXCHANGE);
    }

    @Bean
    public DirectExchange commonExchange() {
        return new DirectExchange(COMMON_EXCHANGE);
    }

    @Bean
    public DirectExchange customerExchange() {
        return new DirectExchange(CUSTOMER_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterExchange");
    }

    // Queues
    @Bean
    public Queue pbIdByNumberQueue() {
        return QueueBuilder.durable(PB_NUMBER_TO_ID_CONVERSION_QUEUE)
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .build();
    }


    /***
     * 메시지가 정상적으로 처리되지 못했을 때, Dead Letter Exchange가 보내는 메시지를 저장하는 큐
     * @return
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build();
    }

    /***
     * Dead Letter Exchange (DLX)와 DLQ 바인딩 
     * @return
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("#");
    }
}
