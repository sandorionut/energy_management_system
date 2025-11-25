package com.ds2025.simulator.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class ConfigLoader {
    private static final String CONFIG_FILE = "config.properties";
    private final Properties properties;
    private double maxConsumption = 0.0;
    private String deviceName = "Unknown";

    public ConfigLoader() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            loadDeviceConfig();
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDeviceConfig() {
        try {
            String selectedId = getDeviceId().toString();
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode devices = mapper.readTree(new java.io.File("devices.json"));

            for (com.fasterxml.jackson.databind.JsonNode device : devices) {
                if (device.get("id").asText().equals(selectedId)) {
                    this.maxConsumption = device.get("maxConsumption").asDouble();
                    this.deviceName = device.get("name").asText();
                    System.out.println("Loaded config for device: " + deviceName + " (Max: " + maxConsumption + ")");
                    return;
                }
            }
            System.err.println("Device ID " + selectedId + " not found in devices.json!");
        } catch (Exception e) {
            System.err.println("Error loading devices.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public UUID getDeviceId() {
        String deviceIdStr = properties.getProperty("device.id");
        if (deviceIdStr == null) {
            throw new IllegalStateException("Please set a valid device.id in config.properties!");
        }
        return UUID.fromString(deviceIdStr);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getRabbitMQHost() {
        return properties.getProperty("rabbitmq.host", "localhost");
    }

    public int getRabbitMQPort() {
        return Integer.parseInt(properties.getProperty("rabbitmq.port", "5672"));
    }

    public String getRabbitMQUsername() {
        return properties.getProperty("rabbitmq.username", "devuser");
    }

    public String getRabbitMQPassword() {
        return properties.getProperty("rabbitmq.password", "devpass");
    }

    public String getRabbitMQQueue() {
        return properties.getProperty("rabbitmq.queue", "device.measurements");
    }

    public int getSimulationIntervalMs() {
        return Integer.parseInt(properties.getProperty("simulation.interval.ms", "20000"));
    }

    public double getMaxConsumption() {
        return maxConsumption;
    }

    public double getNightMin() {
        return Double.parseDouble(properties.getProperty("consumption.night.min", "0.1"));
    }

    public double getNightMax() {
        return Double.parseDouble(properties.getProperty("consumption.night.max", "0.3"));
    }

    public double getMorningMin() {
        return Double.parseDouble(properties.getProperty("consumption.morning.min", "0.4"));
    }

    public double getMorningMax() {
        return Double.parseDouble(properties.getProperty("consumption.morning.max", "0.7"));
    }

    public double getAfternoonMin() {
        return Double.parseDouble(properties.getProperty("consumption.afternoon.min", "0.6"));
    }

    public double getAfternoonMax() {
        return Double.parseDouble(properties.getProperty("consumption.afternoon.max", "1.0"));
    }

    public double getEveningMin() {
        return Double.parseDouble(properties.getProperty("consumption.evening.min", "0.8"));
    }

    public double getEveningMax() {
        return Double.parseDouble(properties.getProperty("consumption.evening.max", "1.2"));
    }
}
