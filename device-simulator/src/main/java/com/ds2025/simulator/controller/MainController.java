package com.ds2025.simulator.controller;

import com.ds2025.simulator.model.DeviceMeasurement;
import com.ds2025.simulator.service.RabbitMQService;
import com.ds2025.simulator.service.SimulationService;
import com.ds2025.simulator.util.ConfigLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController {

    @FXML private Label deviceIdLabel;
    @FXML private Label deviceNameLabel;
    @FXML private Circle statusIndicator;
    @FXML private Label statusLabel;
    @FXML private Button startBtn;
    @FXML private Button stopBtn;
    @FXML private Label totalSentLabel;
    @FXML private Label lastValueLabel;
    @FXML private LineChart<String, Number> consumptionChart;
    @FXML private TextArea logArea;

    private ConfigLoader config;
    private RabbitMQService rabbitMQService;
    private SimulationService simulationService;
    private ScheduledExecutorService scheduler;
    
    private XYChart.Series<String, Number> series;
    private int totalSent = 0;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void initialize() {
        try {
            config = new ConfigLoader();
            rabbitMQService = new RabbitMQService(config);
            simulationService = new SimulationService(config);

            deviceIdLabel.setText(config.getDeviceId().toString());
            deviceNameLabel.setText(config.getDeviceName());

            series = new XYChart.Series<>();
            consumptionChart.getData().add(series);

            log("Application initialized.");
            log("Interval set to: " + config.getSimulationIntervalMs() + " ms");

        } catch (Exception e) {
            log("Error initializing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStart() {
        try {
            if (!rabbitMQService.isConnected()) {
                rabbitMQService.connect();
                log("Connected to RabbitMQ.");
            }

            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(this::performSimulationStep, 0, config.getSimulationIntervalMs(), TimeUnit.MILLISECONDS);

            updateStatus(true);
            log("Simulation STARTED.");
        } catch (Exception e) {
            log("Error starting simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        updateStatus(false);
        log("Simulation STOPPED.");
    }

    private void performSimulationStep() {
        try {
            double value = simulationService.generateConsumptionValue();
            LocalDateTime now = LocalDateTime.now();
            
            DeviceMeasurement measurement = new DeviceMeasurement(
                    config.getDeviceId(),
                    now,
                    value
            );

            rabbitMQService.publish(measurement);

            Platform.runLater(() -> {
                updateUI(now, value);
                log("Sent measurement: " + String.format("%.2f", value) + " kWh");
            });

        } catch (Exception e) {
            Platform.runLater(() -> log("Error sending measurement: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void updateUI(LocalDateTime timestamp, double value) {
        totalSent++;
        totalSentLabel.setText(String.valueOf(totalSent));
        lastValueLabel.setText(String.format("%.2f kWh", value));

        String timeStr = timestamp.format(TIME_FMT);
        series.getData().add(new XYChart.Data<>(timeStr, value));

        if (series.getData().size() > 20) {
            series.getData().remove(0);
        }
    }

    private void updateStatus(boolean running) {
        startBtn.setDisable(running);
        stopBtn.setDisable(!running);
        
        if (running) {
            statusIndicator.setFill(Color.web("#34a853")); 
            statusLabel.setText("Running");
            statusLabel.setTextFill(Color.web("#34a853"));
        } else {
            statusIndicator.setFill(Color.web("#ea4335"));
            statusLabel.setText("Stopped");
            statusLabel.setTextFill(Color.web("#5f6368"));
        }
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FMT);
        logArea.appendText("[" + timestamp + "] " + message + "\n");
    }

    public void shutdown() {
        handleStop();
        if (rabbitMQService != null) {
            rabbitMQService.close();
        }
    }
}
