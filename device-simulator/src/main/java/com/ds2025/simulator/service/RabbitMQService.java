package com.ds2025.simulator.service;

import com.ds2025.simulator.model.DeviceMeasurement;
import com.ds2025.simulator.util.ConfigLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQService {
    private final ConfigLoader config;
    private final ObjectMapper objectMapper;
    private Connection connection;
    private Channel channel;

    public RabbitMQService(ConfigLoader config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getRabbitMQHost());
        factory.setPort(config.getRabbitMQPort());
        factory.setUsername(config.getRabbitMQUsername());
        factory.setPassword(config.getRabbitMQPassword());

        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        
        this.channel.queueDeclare(config.getRabbitMQQueue(), true, false, false, null);
        System.out.println("Connected to RabbitMQ: " + config.getRabbitMQHost());
    }

    public void publish(DeviceMeasurement measurement) throws IOException {
        if (channel == null || !channel.isOpen()) {
            throw new IOException("RabbitMQ channel is not open");
        }

        String json = objectMapper.writeValueAsString(measurement);
        channel.basicPublish("", config.getRabbitMQQueue(), null, json.getBytes());
        System.out.println("Sent: " + json);
    }

    public void close() {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            if (connection != null && connection.isOpen()) connection.close();
        } catch (Exception e) {
            System.err.println("Error closing RabbitMQ connection: " + e.getMessage());
        }
    }
    
    public boolean isConnected() {
        return connection != null && connection.isOpen();
    }
}
