package com.ds2025.authservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.common.lang.NonNull;

@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange syncExchange() {
        return new FanoutExchange("sync.exchange", true, false);
    }

    @Bean
    public Queue authQueue() {
        return new Queue("sync.auth", true);
    }

    @Bean
    public Binding bindAuthQueue(FanoutExchange syncExchange, Queue authQueue) {
        return BindingBuilder.bind(authQueue).to(syncExchange);
    }

    @Bean
    @NonNull
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @SuppressWarnings("null")
    public RabbitTemplate rabbitTemplate(@NonNull ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter());
        template.setExchange("sync.exchange");
        return template;
    }

    @Bean("simpleFactory")
    public SimpleRabbitListenerContainerFactory simpleFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new SimpleMessageConverter());
        return factory;
    }
}

