package com.smartcare.smartcaredesktop.controller;

import com.smartcare.smartcaredesktop.model.User;
import com.smartcare.smartcaredesktop.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ChangePasswordController {

    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    private final UserService userService = new UserService();

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleUpdatePassword() {

        String newPass = newPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        if (newPass.isEmpty() || confirm.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if (!newPass.equals(confirm)) {
            showError("Passwords do not match");
            return;
        }

        userService.updatePassword(currentUser.getId(), newPass);

        redirectToDashboard();
    }

    private void redirectToDashboard() {
        try {

            String role = currentUser.getRoles();
            String fxmlPath;

            if (role.contains("ROLE_ADMIN")) {
                fxmlPath = "/com/smartcare/smartcaredesktop/admin-dashboard.fxml";
            } else if (role.contains("ROLE_DOCTOR")) {
                fxmlPath = "/com/smartcare/smartcaredesktop/doctor-dashboard.fxml";
            } else {
                fxmlPath = "/com/smartcare/smartcaredesktop/patient-dashboard.fxml";
            }

            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource(fxmlPath))
            );

            Stage stage = (Stage) newPasswordField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SmartCare");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}