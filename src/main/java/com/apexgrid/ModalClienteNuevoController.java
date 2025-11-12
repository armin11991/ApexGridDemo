package com.apexgrid;

import com.apexgrid.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModalClienteNuevoController {

    @FXML
    public TextField nombreField;
    @FXML
    public TextField cargoField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField telefonoField;
    @FXML
    public TextField saldoPendienteField;


    public void setCliente(String nombre, String cargo, String correo, String telefono, String saldo) {
        nombreField.setText(nombre);
        cargoField.setText(cargo);
        emailField.setText(correo);
        telefonoField.setText(telefono);
        saldoPendienteField.setText(saldo);
    }

    public void initialize() {
    }

    @FXML
    public void guardarCliente() {
        // Obtener los datos de los campos TextField (no de los Label)
        String nombre = nombreField.getText();
        String cargo = cargoField.getText();
        String correo = emailField.getText();
        String telefono = telefonoField.getText();
        String saldo = saldoPendienteField.getText();

        // Validación de campos
        if (nombre.isEmpty() || cargo.isEmpty() || correo.isEmpty()) {
            // Aquí puedes mostrar un mensaje de error si falta algún campo
            System.out.println("Por favor, complete todos los campos.");
            return;
        }

        // SQL para insertar el cliente (sin el campo cliente_id, usando los campos correctos)
        String insertSQL = "INSERT INTO Clientes (nombre, cargo, email, telefono, saldo_pendiente) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Establecer los parámetros de la consulta SQL
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, cargo);
            preparedStatement.setString(3, correo);
            preparedStatement.setString(4, telefono); // Si es opcional, podrías dejarlo como NULL en caso de que esté vacío
            preparedStatement.setString(5, saldo); // Este es el saldo pendiente

            // Ejecutar la consulta
            preparedStatement.executeUpdate();

            // Actualizar la lista de clientes
            if (clientesController != null) {
                clientesController.cargarClientes(); // Método para cargar la lista de clientes actualizada
            }

            // Cerrar la ventana del modal
            Stage stage = (Stage) nombreField.getScene().getWindow(); // Usar nombreField, que es el TextField
            stage.close();

        } catch (SQLException e) {
            System.err.println("Error al guardar el cliente: " + e.getMessage());
        }
    }

    @FXML
    public void cancelarCliente() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    private ClientesController clientesController;

    public void setClientesController(ClientesController controller) {
        this.clientesController = controller;
    }
}
