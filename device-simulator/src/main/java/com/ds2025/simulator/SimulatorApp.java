package com.ds2025.simulator;

import com.ds2025.simulator.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimulatorApp extends Application {

    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        primaryStage.setTitle("Device Energy Simulator");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        
        primaryStage.setOnCloseRequest(event -> {
            if (controller != null) {
                controller.shutdown();
            }
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
