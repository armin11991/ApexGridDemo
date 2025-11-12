module com.apexgrid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.apexgrid to javafx.fxml;
    exports com.apexgrid;
}