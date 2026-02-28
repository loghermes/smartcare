package com.smartcare.smartcaredesktop.controller;

import com.smartcare.smartcaredesktop.service.DashboardService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label doctorsCountLabel;

    @FXML
    private Label patientsCountLabel;

    @FXML
    private Label appointmentsCountLabel;

    @FXML
    private StackPane contentArea;

    private final DashboardService dashboardService = new DashboardService();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {

        doctorsCountLabel.setText(
                String.valueOf(dashboardService.countDoctors())
        );

        patientsCountLabel.setText(
                String.valueOf(dashboardService.countPatients())
        );

        appointmentsCountLabel.setText(
                String.valueOf(dashboardService.countAppointments())
        );
    }

    @FXML
    private void handleDoctors() {
        loadIntoContent("/com/smartcare/smartcaredesktop/doctor-management.fxml");
    }

    @FXML
    private void handleDashboard() {
        loadScene("/com/smartcare/smartcaredesktop/admin-dashboard.fxml");
    }

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        loadSceneFromEvent(event, "/com/smartcare/smartcaredesktop/login.fxml");
    }

    // ---------- Helper Methods ----------

    private void loadIntoContent(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadScene(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSceneFromEvent(javafx.event.ActionEvent event, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SmartCare Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}