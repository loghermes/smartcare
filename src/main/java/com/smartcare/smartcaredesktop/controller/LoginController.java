package com.smartcare.smartcaredesktop.controller;

import com.smartcare.smartcaredesktop.model.User;
import com.smartcare.smartcaredesktop.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final UserService userService = new UserService();



    @FXML
    private void handleLogin() {

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter email and password");
            return;
        }

        User user = userService.login(email, password);

        if (user == null) {
            showError("Invalid credentials or account disabled");
            return;
        }

        if (Boolean.TRUE.equals(user.getIsFirstLogin())) {
            loadChangePassword(user);
            return;
        }

        redirectByRole(user);
    }

    private void loadChangePassword(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/smartcare/smartcaredesktop/change-password.fxml")
            );

            Scene scene = new Scene(loader.load());

            ChangePasswordController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Change Password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectByRole(User user) {

        String fxmlPath;

        if (user.getRoles().contains("ROLE_ADMIN")) {
            fxmlPath = "/com/smartcare/smartcaredesktop/admin-dashboard.fxml";
        } else if (user.getRoles().contains("ROLE_DOCTOR")) {
            fxmlPath = "/com/smartcare/smartcaredesktop/doctor-dashboard.fxml";
        } else if (user.getRoles().contains("ROLE_PATIENT")) {
            fxmlPath = "/com/smartcare/smartcaredesktop/patient-dashboard.fxml";
        } else {
            showError("Unauthorized role");
            return;
        }

        try {
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource(fxmlPath))
            );

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SmartCare");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}