package com.apexgrid;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClienteCardController {

    @FXML private Label nombreLabel;
    @FXML private Label cargoLabel;
    @FXML private Label correoLabel;
    @FXML private Label telefonoLabel;
    @FXML private Label saldoLabel;

    private ClientesController clientesController;  // Propiedad para el controlador de clientes

    // Setter para asignar el controlador de clientes
    public void setClientesController(ClientesController clientesController) {
        this.clientesController = clientesController;
    }

    public void setNombre(String nombre) {
        nombreLabel.setText(nombre);
    }

    public void setCargo(String cargo) {
        cargoLabel.setText(cargo);
    }

    public void setCorreo(String correo) {
        correoLabel.setText(correo);
    }

    public void setTelefono(String telefono) {
        telefonoLabel.setText(telefono);
    }

    public void setSaldo(String saldo) {
        saldoLabel.setText(saldo);
    }

    // Método llamado al hacer clic en el botón "Editar"
    @FXML
    public void editarCliente() {
        if (clientesController != null) {
            clientesController.crearCliente();  // Llamamos a crearCliente del ClientesController
        }
    }
}