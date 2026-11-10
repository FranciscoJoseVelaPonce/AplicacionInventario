package org.example.aplicacioninventario;

import javafx.application.Application;
import javafx.stage.Stage;

public class InventarioAplicacion extends Application {

    @Override
    public void start(Stage escenario) {
        NavegacionControlador.setEscenario(escenario);
        NavegacionControlador.navegarA("login-vista.fxml", "Inventario - Login");
        escenario.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
