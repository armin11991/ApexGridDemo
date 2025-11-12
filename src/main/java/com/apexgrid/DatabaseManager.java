package com.apexgrid;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class DatabaseManager {

    private static final String DATABASE_FILE_NAME = "apexgrid.db";
    private static final String DATABASE_RESOURCE_PATH = "com/apexgrid/" + DATABASE_FILE_NAME;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");

            // Definir la ruta de datos de la aplicación
            String userHome = System.getProperty("user.home");
            File appDataDir = new File(userHome, "ApexGrid");

            // Crear el directorio si no existe
            if (!appDataDir.exists()) {
                appDataDir.mkdirs();
            }

            // Definir la ruta del archivo de base de datos
            File dbFile = new File(appDataDir, DATABASE_FILE_NAME);

            // Si la base de datos no existe aún, inicialízala copiando desde recursos
            if (!dbFile.exists()) {
                initializeDatabase(dbFile);
            }

            // Conectarse a la base de datos
            String dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            return DriverManager.getConnection(dbUrl);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            throw new SQLException(e);
        }
    }

    private static void initializeDatabase(File dbFile) {
        try (InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream(DATABASE_RESOURCE_PATH)) {
            if (inputStream != null) {
                // Copiar desde el recurso al archivo, reemplazando si existe
                Files.copy(inputStream, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Base de datos inicial copiada desde los recursos.");
            } else {
                // Si el recurso no se encuentra, crear una nueva base de datos con el esquema inicial
                System.out.println("No se encontró la base de datos inicial en los recursos. Creando una nueva.");
                createNewDatabase(dbFile);
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createNewDatabase(File dbFile) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
             Statement stmt = conn.createStatement()) {
            // Crear las tablas con sentencias SQL
            stmt.execute("CREATE TABLE IF NOT EXISTS Clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, cargo TEXT, email TEXT, telefono TEXT, saldo_pendiente TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS Proyectos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, cliente_id INTEGER, descripcion TEXT, fecha_inicio TEXT, estado TEXT, FOREIGN KEY (cliente_id) REFERENCES Clientes(id));");
            stmt.execute("CREATE TABLE IF NOT EXISTS Presupuestos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, cliente_id INTEGER, descripcion TEXT, fecha_inicio TEXT, monto TEXT, estado TEXT, FOREIGN KEY (cliente_id) REFERENCES Clientes(id));");
            System.out.println("Nueva base de datos creada con el esquema inicial.");
            insertInitialData(conn); // Insertar datos iniciales al crear la base de datos
        }
    }

    private static void insertInitialData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Insertar algunos clientes
            String insertClientesSQL =
                    "INSERT INTO Clientes (nombre, cargo, email, telefono, saldo_pendiente) VALUES " +
                            "('Cliente 1', 'Gerente', 'cliente1@empresa.com', '123456789', '1000')," +
                            "('Cliente 2', 'Director', 'cliente2@empresa.com', '987654321', '1500')," +
                            "('Cliente 3', 'Jefe de Proyecto', 'cliente3@empresa.com', '123123123', '500')," +
                            "('Cliente 4', 'Consultor', 'cliente4@empresa.com', '321321321', '300')," +
                            "('Cliente 5', 'Director de Marketing', 'cliente5@empresa.com', '555555555', '700')," +
                            "('Cliente 6', 'CEO', 'cliente6@empresa.com', '666666666', '1200')," +
                            "('Cliente 7', 'Jefe de Tecnología', 'cliente7@empresa.com', '777777777', '400')," +
                            "('Cliente 8', 'Coordinador', 'cliente8@empresa.com', '888888888', '0')," +
                            "('Cliente 9', 'Asesor', 'cliente9@empresa.com', '999999999', '250');";
            statement.execute(insertClientesSQL);

            // Insertar algunos proyectos
            String insertProyectosSQL =
                    "INSERT INTO Proyectos (nombre, cliente_id, descripcion, fecha_inicio, estado) VALUES " +
                            "('Proyecto A', 1, 'Proyecto de construcción', '2025-04-19', 'En progreso')," +
                            "('Proyecto B', 2, 'Desarrollo de software', '2025-03-10', 'Pendiente')," +
                            "('Proyecto C', 3, 'Consultoría en marketing', '2025-02-15', 'Finalizado');";
            statement.execute(insertProyectosSQL);

            // Insertar algunos presupuestos
            String insertPresupuestosSQL =
                    "INSERT INTO Presupuestos (nombre, cliente_id, descripcion, fecha_inicio, monto, estado) VALUES " +
                            "('Presupuesto A1', 1, 'Presupuesto para construcción', '2025-04-01', '20000', 'Pendiente cobro')," +
                            "('Presupuesto A2', 1, 'Presupuesto para remodelación', '2025-04-05', '5000', 'Pagado')," +
                            "('Presupuesto B1', 2, 'Presupuesto para software', '2025-03-12', '10000', 'Pendiente cobro')," +
                            "('Presupuesto B2', 2, 'Presupuesto para soporte', '2025-03-20', '3000', 'Pagado')," +
                            "('Presupuesto C1', 3, 'Presupuesto para consultoría', '2025-02-10', '8000', 'Pendiente cobro')," +
                            "('Presupuesto C2', 3, 'Presupuesto para publicidad', '2025-02-18', '6000', 'Pagado');";
            statement.execute(insertPresupuestosSQL);

            System.out.println("Datos iniciales insertados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar datos iniciales: " + e.getMessage());
        }
    }

    // No es necesario llamar a createTables() explícitamente si la base de datos se copia o se crea nueva
    // public static void createTables() { ... }
}