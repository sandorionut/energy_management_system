package com.ds2025.simulator.service;

import com.ds2025.simulator.util.ConfigLoader;

import java.time.LocalTime;
import java.util.Random;

public class SimulationService {
    private final ConfigLoader config;
    private final Random random;

    public SimulationService(ConfigLoader config) {
        this.config = config;
        this.random = new Random();
    }

    public double generateConsumptionValue() {
        LocalTime now = LocalTime.now();
        double min, max;

        if (isBetween(now, LocalTime.of(0, 0), LocalTime.of(6, 0))) {
            min = config.getNightMin();
            max = config.getNightMax();
        } else if (isBetween(now, LocalTime.of(6, 0), LocalTime.of(12, 0))) {
            min = config.getMorningMin();
            max = config.getMorningMax();
        } else if (isBetween(now, LocalTime.of(12, 0), LocalTime.of(18, 0))) {
            min = config.getAfternoonMin();
            max = config.getAfternoonMax();
        } else {
            min = config.getEveningMin();
            max = config.getEveningMax();
        }

        double randomPercentage = min + (max - min) * random.nextDouble();
        
        double value = config.getMaxConsumption() * randomPercentage;

        double noise = value * 0.05 * (random.nextDouble() * 2 - 1);
        
        return Math.max(0, value + noise);
    }

    private boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
        return !time.isBefore(start) && time.isBefore(end);
    }
}
