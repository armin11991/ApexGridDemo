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

public class PresupuestosController {

    @FXML
    private TableView<Presupuesto> presupuestosTableView;
    @FXML
    private TableColumn<Presupuesto, Integer> idColumnPres;
    @FXML
    private TableColumn<Presupuesto, String> nombreColumnPres;
    @FXML
    private TableColumn<Presupuesto, String> clienteColumnPres;
    @FXML
    private TableColumn<Presupuesto, String> descripcionColumnPres;
    @FXML
    private TableColumn<Presupuesto, String> fechaColumnPres;
    @FXML
    private TableColumn<Presupuesto, String> montoColumnPres;
    @FXML
    private TableColumn<Presupuesto, String> estadoColumnPres;
    @FXML
    private TableColumn<Presupuesto, Void> editarColumnPres;

    @FXML
    public void initialize() {
        // Configurar las columnas de la TableView
        idColumnPres.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumnPres.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        clienteColumnPres.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        descripcionColumnPres.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        fechaColumnPres.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        montoColumnPres.setCellValueFactory(new PropertyValueFactory<>("monto"));
        estadoColumnPres.setCellValueFactory(new PropertyValueFactory<>("estado"));
        // Configurar la columna de "Editar" para mostrar un botón
        editarColumnPres.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));

        editarColumnPres.setCellFactory(param -> new TableCell<Presupuesto, Void>() {
            private final Button editarButton = new Button("Editar");

            {
                editarButton.setOnAction(event -> {
                    Presupuesto presupuesto = getTableRow().getItem();
                    if (presupuesto != null) {
                        crearPresupuesto();
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
        cargarPresupuestos();
    }

    public void cargarPresupuestos() {
        ObservableList<Presupuesto> presupuestos = FXCollections.observableArrayList();

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Presupuestos")) {

            while (resultSet.next()) {
                Presupuesto presupuesto = new Presupuesto(
                        resultSet.getInt("id"), // Asegúrate de incluir el id
                        resultSet.getString("nombre"),
                        resultSet.getString("cliente_id"),
                        resultSet.getString("descripcion"),
                        resultSet.getString("fecha_inicio"),
                        resultSet.getString("monto"),
                        resultSet.getString("estado"),
                        "..." // Valor de ejemplo para la columna "Editar"
                );
                presupuestos.add(presupuesto);
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar los presupuestos: " + e.getMessage());
            e.printStackTrace();
        }

        presupuestosTableView.setItems(presupuestos);
    }

    @FXML
    private void mostrarProyectos(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ProyectosView.fxml"));
            Scene nuevaEscena = new Scene(loader.load());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Guardar tamaño actual
            double ancho = currentStage.getWidth();
            double alto = currentStage.getHeight();

            // Aplicar nueva escena
            currentStage.setScene(nuevaEscena);
            currentStage.setTitle("Proyectos");

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
    public void crearPresupuesto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apexgrid/ModalPresupuestoNuevo.fxml"));
            Parent root = loader.load();

            ModalPresupuestoNuevoController modalController = loader.getController();

            // PASAR la instancia actual del controlador al modal
            modalController.setPresupuestosController(this);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Crear/Editar Presupuesto");
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

    // Clase Presupuesto para representar los datos de la TableView
    public static class Presupuesto {
        private int id;
        private String nombre;
        private String cliente;
        private String descripcion;
        private String fechaInicio;
        private String monto;
        private String estado;
        private String editar;

        public Presupuesto(int id, String nombre, String cliente, String descripcion, String fechaInicio, String monto, String estado, String editar) {
            this.id = id;
            this.nombre = nombre;
            this.cliente = cliente;
            this.descripcion = descripcion;
            this.fechaInicio = fechaInicio;
            this.monto = monto;
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

        public String getMonto() { return monto; }
        public void setMonto(String monto) { this.monto = monto; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getEditar() { return editar; }
        public void setEditar(String editar) { this.editar = editar; }
    }
}
