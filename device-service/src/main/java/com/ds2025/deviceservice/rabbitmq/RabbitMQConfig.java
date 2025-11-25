package com.ds2025.deviceservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange syncExchange() {
        return new FanoutExchange("sync.exchange", true, false);
    }

    @Bean
    public Queue deviceQueue() {
        return new Queue("sync.device", true);
    }

    @Bean
    public Binding bindDeviceQueue(FanoutExchange exchange, Queue deviceQueue) {
        return BindingBuilder.bind(deviceQueue).to(exchange);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    @SuppressWarnings("null")
    public RabbitTemplate rabbitTemplate(@NonNull ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(rabbitMessageConverter());
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

