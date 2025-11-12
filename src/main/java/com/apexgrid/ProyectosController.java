package com.apexgrid;

import com.apexgrid.DatabaseManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProyectosController {

    @FXML
    private TableView<Proyecto> proyectosTableView;
    @FXML
    private TableColumn<Proyecto, Integer> idColumn;
    @FXML
    private TableColumn<Proyecto, String> nombreColumn;
    @FXML
    private TableColumn<Proyecto, String> clienteColumn;
    @FXML
    private TableColumn<Proyecto, String> descripcionColumn;
    @FXML
    private TableColumn<Proyecto, String> fechaInicioColumn;
    @FXML
    private TableColumn<Proyecto, String> estadoColumn;
    @FXML
    private TableColumn<Proyecto, Void> editarColumn;

    @FXML
    public void initialize() {
        // Configurar las columnas de la TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configurar la columna de "Editar" para mostrar un botón
        editarColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));

        editarColumn.setCellFactory(param -> new TableCell<Proyecto, Void>() {
            private final Button editarButton = new Button("Editar");

            {
                editarButton.setOnAction(event -> {
                    Proyecto proyecto = getTableRow().getItem();
                    if (proyecto != null) {
                        // Aquí se llama al método como si fuera onAction="#crearProyecto"
                        crearProyecto();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editarButton);
                }
            }
        });

        // Cargar los datos de los proyectos desde la base de datos
        cargarProyectos();
    }

    public void cargarProyectos() {
        ObservableList<Proyecto> proyectos = FXCollections.observableArrayList();

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Proyectos")) {

            while (resultSet.next()) {
                Proyecto proyecto = new Proyecto(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("cliente_id"),
                        resultSet.getString("descripcion"),
                        resultSet.getString("fecha_inicio"),
                        resultSet.getString("estado"),
                        "..." // Valor para la columna "Editar"
                );
                proyectos.add(proyecto);
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar los proyectos: " + e.getMessage());
            e.printStackTrace();
        }

        proyectosTableView.setItems(proyectos);
    }

    @FXML
    private void mostrarPresupuestos(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/PresupuestosView.fxml"));
            Scene nuevaEscena = new Scene(loader.load());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Guardar tamaño actual
            double ancho = currentStage.getWidth();
            double alto = currentStage.getHeight();

            // Aplicar nueva escena
            currentStage.setScene(nuevaEscena);
            currentStage.setTitle("Presupuestos");

            // Restaurar tamaño
            currentStage.setWidth(ancho);
            currentStage.setHeight(alto);

            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarClientes(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ClientesView.fxml"));
            Scene nuevaEscena = new Scene(loader.load());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Guardar tamaño actual
            double ancho = currentStage.getWidth();
            double alto = currentStage.getHeight();

            // Aplicar nueva escena
            currentStage.setScene(nuevaEscena);
            currentStage.setTitle("Clientes");

            // Restaurar tamaño
            currentStage.setWidth(ancho);
            currentStage.setHeight(alto);

            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void crearProyecto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ModalProyectoNuevo.fxml"));
            Parent root = loader.load();

            ModalProyectoNuevoController modalController = loader.getController();

            // PASAR la instancia actual del controlador al modal
            modalController.setProyectosController(this);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Crear/Editar Proyecto");
            modalStage.setScene(new Scene(root));
            modalStage.show();

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

    // Clase Proyecto para representar los datos de la TableView
    public static class Proyecto {
        private int id;
        private String nombre;
        private String cliente;
        private String descripcion;
        private String fechaInicio;
        private String estado;
        private String editar;

        public Proyecto(int id, String nombre, String cliente, String descripcion, String fechaInicio, String estado, String editar) {
            this.id = id;
            this.nombre = nombre;
            this.cliente = cliente;
            this.descripcion = descripcion;
            this.fechaInicio = fechaInicio;
            this.estado = estado;
            this.editar = editar;
        }

        // Getters y setters para cada campo
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public String getFechaInicio() { return fechaInicio; }
        public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getEditar() { return editar; }
        public void setEditar(String editar) { this.editar = editar; }
    }
}