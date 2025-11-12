package com.apexgrid;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomAlertController {

    @FXML
    private Label lblMensaje;

    @FXML
    private Button btnAceptar;

    public void setMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    @FXML
    private void cerrarAlerta(javafx.event.ActionEvent event) {
        // Cierra la ventana emergente
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
