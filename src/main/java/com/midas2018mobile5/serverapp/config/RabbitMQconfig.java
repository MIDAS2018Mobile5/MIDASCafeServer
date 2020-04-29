package com.midas2018mobile5.serverapp.config;

import com.midas2018mobile5.serverapp.dao.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Neon K.I.D on 4/29/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Configuration
@RequiredArgsConstructor
public class RabbitMQconfig {
    private final ConnectionFactory connectionFactory;

    @Bean
    public Queue newOrdered() {
        return QueueBuilder.durable(OrderEvent.orderProcessBasket).build();
    }

    @Bean
    public RabbitTemplate amqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setMessageConverter(queueMessageConverter());

        return rabbitTemplate;
    }

    private Jackson2JsonMessageConverter queueMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
