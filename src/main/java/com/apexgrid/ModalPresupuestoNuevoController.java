package com.apexgrid;

import com.apexgrid.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModalPresupuestoNuevoController {

    @FXML
    public TextField nombrePresupuestoField;
    @FXML
    public TextField clienteField;
    @FXML
    public TextArea descripcionArea;
    @FXML
    public DatePicker fechaInicioPicker;
    @FXML
    public TextField montoField;
    @FXML
    public ComboBox<EstadoPresupuesto> estadoPresupuestoComboBox;

    private TableView<PresupuestosController.Presupuesto> presupuestosTableView; // Referencia a la TableView de presupuestos

    public void setPresupuestosTableView(TableView<PresupuestosController.Presupuesto> presupuestosTableView) {
        this.presupuestosTableView = presupuestosTableView;
    }

    public void initialize() {
        estadoPresupuestoComboBox.setItems(FXCollections.observableArrayList(
                new EstadoPresupuesto("Pendiente", Color.RED),
                new EstadoPresupuesto("Pagado", Color.GREEN)
        ));

        estadoPresupuestoComboBox.setCellFactory(param -> new ListCell<EstadoPresupuesto>() {
            @Override
            protected void updateItem(EstadoPresupuesto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Circle circle = new Circle(8, item.getColor());
                    Label label = new Label(item.getNombre());
                    HBox hbox = new HBox(circle, label);
                    hbox.setSpacing(10);
                    setGraphic(hbox);
                }
            }
        });

        estadoPresupuestoComboBox.setButtonCell(new ListCell<EstadoPresupuesto>() {
            @Override
            protected void updateItem(EstadoPresupuesto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Circle circle = new Circle(8, item.getColor());
                    Label label = new Label(item.getNombre());
                    HBox hbox = new HBox(circle, label);
                    hbox.setSpacing(10);
                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    public void guardarPresupuesto() {
        String nombre = nombrePresupuestoField.getText();
        String cliente = clienteField.getText();
        String descripcion = descripcionArea.getText();
        String fechaInicio = fechaInicioPicker.getValue() != null ? fechaInicioPicker.getValue().toString() : null;
        String monto = montoField.getText();
        String estado = estadoPresupuestoComboBox.getValue() != null ? estadoPresupuestoComboBox.getValue().getNombre() : null;

        // Validación de campos
        if (nombre.isEmpty() || cliente.isEmpty() || descripcion.isEmpty() || fechaInicio == null || monto.isEmpty() || estado == null) {
            // Aquí puedes mostrar un mensaje de error si falta algún campo
            System.out.println("Por favor, complete todos los campos.");
            return;
        }

        String insertSQL = "INSERT INTO Presupuestos (nombre, cliente_id, descripcion, fecha_inicio, monto, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, cliente);  // Se guarda como texto
            preparedStatement.setString(3, descripcion);
            preparedStatement.setString(4, fechaInicio);
            preparedStatement.setString(5, monto);
            preparedStatement.setString(6, estado);

            preparedStatement.executeUpdate();

            if (presupuestosController != null) {
                presupuestosController.cargarPresupuestos(); // Método para cargar la lista actualizada
            }

            // Cerrar la ventana del modal
            Stage stage = (Stage) nombrePresupuestoField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            System.err.println("Error al guardar el presupuesto: " + e.getMessage());
        }
    }

    @FXML
    public void cancelarPresupuesto() {
        Stage stage = (Stage) nombrePresupuestoField.getScene().getWindow();
        stage.close();
    }

    public static class EstadoPresupuesto {
        private String nombre;
        private Color color;

        public EstadoPresupuesto(String nombre, Color color) {
            this.nombre = nombre;
            this.color = color;
        }

        public String getNombre() {
            return nombre;
        }

        public Color getColor() {
            return color;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    private PresupuestosController presupuestosController;

    public void setPresupuestosController(PresupuestosController controller) {
        this.presupuestosController = controller;
    }

}
