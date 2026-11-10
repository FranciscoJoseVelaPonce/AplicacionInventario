module org.example.aplicacioninventario {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires mysql.connector.j;
    requires java.naming;

    opens org.example.aplicacioninventario to javafx.fxml;
    exports org.example.aplicacioninventario;
    exports cesur.dam.g1.models;
    opens cesur.dam.g1.models to javafx.fxml;
}
