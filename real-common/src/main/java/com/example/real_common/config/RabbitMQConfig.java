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
    public static final String CONSULTING_EXCHANGE = "realtime.consulting.exchange";
    // Routing keys
    public static final String PB_NUMBER_TO_ID_ROUTING_KEY = "pbRoutingKey.pbNumber";
    public static final String PB_TOKEN_TO_ID_ROUTING_KEY = "pbTokenRoutingKey.pbToken";
    public static final String CUSTOMER_TOKEN_TO_ID_ROUTING_KEY = "customerTokenRoutingKey.customerToken";
    public static final String CUSTOMER_TOKEN_TO_NAME_ROUTING_KEY = "customerTokenTokenNameRoutingKey.customerToken";
    public static final String CONSULTING_ROOM_CREATION_ROUNTING_KEY = "transferRoomInfoRoutingKey";

    // Queue names
    public static final String PB_NUMBER_TO_ID_CONVERSION_QUEUE = "pbIdbyNumberQueue";
    public static final String PB_TOKEN_TO_ID_CONVERSION_QUEUE = "pbIdbyTokenQueue";
    public static final String CUSTOMER_TOKEN_TO_ID_CONVERSION_QUEUE = "customerIdbyTokenQueue";
    public static final String CUSTOMER_TOKEN_TO_NAME_CONVERSION_QUEUE = "customerTokenNameQueue";
    public static final String CONSULTING_ROOM_CREATION_QUEUE = "consultingRoomCreationQueue";

    //constants
    public static final int INVALID_PB_NUMBER = -1;
    public static final int INVALID_TOKEN = -1;
    public static final int INVALID_VALUE = -1;

    public static final String UNKNOWN_CUSTOMER ="Unknown Customer";

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

    // Queues
    @Bean
    public Queue pbIdByNumberQueue() {
        return QueueBuilder.durable(PB_NUMBER_TO_ID_CONVERSION_QUEUE).build();
    }

    @Bean
    public Queue pbIdByTokenQueue() {
        return QueueBuilder.durable(PB_TOKEN_TO_ID_CONVERSION_QUEUE).build();
    }

    @Bean
    public Queue customerIdbyNumberQueue() {
        return QueueBuilder.durable(CUSTOMER_TOKEN_TO_ID_CONVERSION_QUEUE).build();
    }

    @Bean
    public Queue customerNameByTokenQueue() {
        return QueueBuilder.durable(CUSTOMER_TOKEN_TO_NAME_CONVERSION_QUEUE).build();
    }

    @Bean
    public Queue consultingRoomInfoQueue() {
        return QueueBuilder.durable(CONSULTING_ROOM_CREATION_QUEUE).build();
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
        return BindingBuilder.bind(customerIdbyNumberQueue())
                .to(customerExchange())
                .with(CUSTOMER_TOKEN_TO_ID_ROUTING_KEY);
    }

    @Bean
    public Binding customerTokenNamefoBinding() {
        return BindingBuilder.bind(customerNameByTokenQueue())
                .to(customerExchange())
                .with(CUSTOMER_TOKEN_TO_NAME_ROUTING_KEY);
    }

    @Bean
    public Binding consultingRoomInfoBinding() {
        return BindingBuilder.bind(consultingRoomInfoQueue())
                .to(consultingExchange())
                .with(CONSULTING_ROOM_CREATION_ROUNTING_KEY);
    }

}
