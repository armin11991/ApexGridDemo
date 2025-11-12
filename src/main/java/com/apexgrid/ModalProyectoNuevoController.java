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

public class ModalProyectoNuevoController {

    @FXML
    public TextField nombreProyectoField;
    @FXML
    public TextField clienteField;
    @FXML
    public TextArea descripcionArea;
    @FXML
    public DatePicker fechaInicioPicker;
    @FXML
    public ComboBox<EstadoProyecto> estadoComboBox;

    private TableView<ProyectosController.Proyecto> proyectosTableView; // Referencia a la TableView de proyectos

    public void setProyectosTableView(TableView<ProyectosController.Proyecto> proyectosTableView) {
        this.proyectosTableView = proyectosTableView;
    }

    public void initialize() {
        estadoComboBox.setItems(FXCollections.observableArrayList(
                new EstadoProyecto("Pendiente", Color.RED),
                new EstadoProyecto("En Proceso", Color.YELLOW),
                new EstadoProyecto("Finalizado", Color.GREEN)
        ));

        estadoComboBox.setCellFactory(param -> new ListCell<EstadoProyecto>() {
            @Override
            protected void updateItem(EstadoProyecto item, boolean empty) {
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

        estadoComboBox.setButtonCell(new ListCell<EstadoProyecto>() {
            @Override
            protected void updateItem(EstadoProyecto item, boolean empty) {
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
    public void guardarProyecto() {
        String nombre = nombreProyectoField.getText();
        String cliente = clienteField.getText();
        String descripcion = descripcionArea.getText();
        String fechaInicio = fechaInicioPicker.getValue() != null ? fechaInicioPicker.getValue().toString() : null;
        String estado = estadoComboBox.getValue() != null ? estadoComboBox.getValue().getNombre() : null;

        String insertSQL = "INSERT INTO Proyectos (nombre, cliente_id, descripcion, fecha_inicio, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, cliente);
            preparedStatement.setString(3, descripcion);
            preparedStatement.setString(4, fechaInicio);
            preparedStatement.setString(5, estado);

            preparedStatement.executeUpdate();

            if (proyectosController != null) {
                proyectosController.cargarProyectos();
            }

            // Cerrar la ventana del modal
            Stage stage = (Stage) nombreProyectoField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            System.err.println("Error al guardar el proyecto: " + e.getMessage());
        }
    }

    @FXML
    public void cancelarProyecto() {
        Stage stage = (Stage) nombreProyectoField.getScene().getWindow();
        stage.close();
    }

    public static class EstadoProyecto {
        private String nombre;
        private Color color;

        public EstadoProyecto(String nombre, Color color) {
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

    private ProyectosController proyectosController;

    public void setProyectosController(ProyectosController controller) {
        this.proyectosController = controller;
    }

}
