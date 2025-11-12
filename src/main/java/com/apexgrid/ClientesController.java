package com.apexgrid;

import com.apexgrid.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientesController {

    @FXML
    private GridPane clientesGridPane;

    @FXML
    public void initialize() {
        // Cargar los datos de los clientes desde la base de datos
        cargarClientes();
    }

    @FXML
    public void cargarClientes() {
        clientesGridPane.getChildren().clear(); // Limpiar el grid

        String query = "SELECT nombre, cargo, email, telefono, saldo_pendiente FROM Clientes";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int column = 0;
            int row = 0;

            while (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ClienteCard.fxml"));
                VBox clienteCard = loader.load();

                ClienteCardController controller = loader.getController();
                controller.setNombre(rs.getString("nombre"));
                controller.setCargo(rs.getString("cargo"));
                controller.setCorreo(rs.getString("email"));
                controller.setTelefono(rs.getString("telefono"));
                controller.setSaldo(rs.getString("saldo_pendiente"));

                // Pasar la instancia del ClientesController a cada tarjeta
                controller.setClientesController(this);

                clientesGridPane.add(clienteCard, column, row);

                column++;
                if (column == 5) {
                    column = 0;
                    row++;
                }
            }

        } catch (IOException | SQLException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
    }

    @FXML
    private void mostrarPresupuestos(MouseEvent event) {
        try {
            // Cargar la nueva vista desde el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/PresupuestosView.fxml"));
            Scene nuevaEscena = new Scene(loader.load());

            // Obtener el Stage actual desde el evento
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Establecer la nueva escena en el mismo Stage (ventana)
            currentStage.setScene(nuevaEscena);
            currentStage.setTitle("Presupuestos");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarProyectos(MouseEvent event) {
        try {
            // Cargar la nueva vista desde el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ProyectosView.fxml"));
            Scene nuevaEscena = new Scene(loader.load());

            // Obtener el Stage actual desde el evento
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Establecer la nueva escena en el mismo Stage (ventana)
            currentStage.setScene(nuevaEscena);
            currentStage.setTitle("Proyectos");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostarSignInView(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/SignInView.fxml"));
            Scene nuevaEscena = new Scene(loader.load());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Guardar tamaño actual
            double ancho = currentStage.getWidth();
            double alto = currentStage.getHeight();

            // Aplicar nueva escena
            currentStage.setScene(nuevaEscena);
            currentStage.setTitle("SignIn");

            // Restaurar tamaño
            currentStage.setWidth(ancho);
            currentStage.setHeight(alto);

            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void crearCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ModalClienteNuevo.fxml"));
            Parent root = loader.load();

            ModalClienteNuevoController modalController = loader.getController();

            // PASAR la instancia actual del controlador al modal
            modalController.setClientesController(this);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Crear/Editar Cliente");
            modalStage.setScene(new Scene(root));
            modalStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase Cliente para representar los datos de la TableView
    public static class Cliente {
        private int id;
        private String nombre;
        private String cargo;
        private String correo;
        private String telefono;
        private String saldoPendiente;

        public Cliente(int id, String nombre, String cargo, String correo, String telefono, String saldoPendiente) {
            this.id = id;
            this.nombre = nombre;
            this.cargo = cargo;
            this.correo = correo;
            this.telefono = telefono;
            this.saldoPendiente = saldoPendiente;
        }

        // Getters y setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCargo() { return cargo; }
        public void setCargo(String cargo) { this.cargo = cargo; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getSaldoPendiente() { return saldoPendiente; }
        public void setSaldoPendiente(String saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    }
}