package com.example.real_common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Exchange names
    public static final String PB_EXCHANGE = "pb.direct.exchange_prod";
    public static final String COMMON_EXCHANGE = "common.direct.exchange_prod";
    public static final String CUSTOMER_EXCHANGE = "customer.direct.exchange_prod";
    public static final String CONSULTING_EXCHANGE = "realtime.consulting.exchange_prod";
    public static final String DEAD_LETTER_EXCHANGE = "deadLetterExchange_prod";

    // Routing keys
    public static final String PB_NUMBER_TO_ID_ROUTING_KEY = "pbRoutingKey.pbNumber_prod";
    public static final String PB_TOKEN_TO_ID_ROUTING_KEY = "pbTokenRoutingKey.pbToken_prod";
    public static final String CUSTOMER_TOKEN_TO_ID_ROUTING_KEY = "customerTokenRoutingKey.customerToken_prod";
    public static final String CUSTOMER_TOKEN_TO_NAME_ROUTING_KEY = "customerTokenNameRoutingKey.customerToken_prod";
    public static final String CONSULTING_ROOM_CREATION_ROUTING_KEY = "transferRoomInfoRoutingKey_prod";
    public static final String DEAD_LETTER_ROUTING_KEY = "deadLetterRoutingKey_prod";

    // Queue names
    public static final String PB_NUMBER_TO_ID_CONVERSION_QUEUE = "pbIdbyNumberQueue_prod";
    public static final String PB_TOKEN_TO_ID_CONVERSION_QUEUE = "pbIdbyTokenQueue_prod";
    public static final String CUSTOMER_TOKEN_TO_ID_CONVERSION_QUEUE = "customerIdbyTokenQueue_prod";
    public static final String CUSTOMER_TOKEN_TO_NAME_CONVERSION_QUEUE = "customerTokenNameQueue_prod";
    public static final String CONSULTING_ROOM_CREATION_QUEUE = "consultingRoomCreationQueue_prod";
    public static final String DEAD_LETTER_QUEUE_NAME = "deadLetterQueue_prod";

    // Constants
    public static final int INVALID_PB_NUMBER = -1;
    public static final int INVALID_TOKEN = -1;
    public static final int INVALID_VALUE = -1;
    public static final String UNKNOWN_CUSTOMER = "Unknown Customer";

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
    public DirectExchange consultingExchange() {
        return new DirectExchange(CONSULTING_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // Queues
    @Bean
    public Queue pbIdByNumberQueue() {
        return QueueBuilder.durable(PB_NUMBER_TO_ID_CONVERSION_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue pbIdByTokenQueue() {
        return QueueBuilder.durable(PB_TOKEN_TO_ID_CONVERSION_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue customerIdByTokenQueue() {
        return QueueBuilder.durable(CUSTOMER_TOKEN_TO_ID_CONVERSION_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue customerNameByTokenQueue() {
        return QueueBuilder.durable(CUSTOMER_TOKEN_TO_NAME_CONVERSION_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue consultingRoomInfoQueue() {
        return QueueBuilder.durable(CONSULTING_ROOM_CREATION_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build();
    }

    // Bindings
    @Bean
    public Binding pbNumberToIdBinding() {
        return BindingBuilder.bind(pbIdByNumberQueue())
                .to(pbExchange())
                .with(PB_NUMBER_TO_ID_ROUTING_KEY);
    }

    @Bean
    public Binding pbTokenToIdBinding() {
        return BindingBuilder.bind(pbIdByTokenQueue())
                .to(pbExchange())
                .with(PB_TOKEN_TO_ID_ROUTING_KEY);
    }

    @Bean
    public Binding customerTokenToIdBinding() {
        return BindingBuilder.bind(customerIdByTokenQueue())
                .to(customerExchange())
                .with(CUSTOMER_TOKEN_TO_ID_ROUTING_KEY);
    }

    @Bean
    public Binding customerTokenNameBinding() {
        return BindingBuilder.bind(customerNameByTokenQueue())
                .to(customerExchange())
                .with(CUSTOMER_TOKEN_TO_NAME_ROUTING_KEY);
    }

    @Bean
    public Binding consultingRoomInfoBinding() {
        return BindingBuilder.bind(consultingRoomInfoQueue())
                .to(consultingExchange())
                .with(CONSULTING_ROOM_CREATION_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}
