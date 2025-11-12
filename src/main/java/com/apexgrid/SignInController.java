package com.apexgrid;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Por favor, completa todos los campos.");
            return;
        }

        if (username.equals("admin") && password.equals("1234")) {
            abrirProyectos();
        } else {
            mostrarAlerta("Usuario o contraseña incorrectos.");
        }
    }

    private void abrirProyectos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ProyectosView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Proyectos");
            stage.show();

            // Cerrar la ventana de login
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/CustomAlert.fxml"));
            Region root = loader.load();

            CustomAlertController controller = loader.getController();
            controller.setMensaje(mensaje);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT); // Para permitir esquinas redondeadas y fondo personalizado

            // Crear la escena con fondo transparente
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            dialog.setScene(scene);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarAlertaConCredenciales(ActionEvent event) {
        mostrarAlerta("usuario: admin / contraseña: 1234");
    }

}