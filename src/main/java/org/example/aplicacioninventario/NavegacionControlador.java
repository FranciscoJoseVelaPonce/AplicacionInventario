package org.example.aplicacioninventario;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavegacionControlador {

    private static Stage escenario;

    public static void setEscenario(Stage escenario) {
        NavegacionControlador.escenario = escenario;
    }

    public static void navegarA(String fxml, String titulo) {
        FXMLLoader cargador = new FXMLLoader(InventarioAplicacion.class.getResource(fxml));
        Scene escena = null;
        try {
            escena = new Scene(cargador.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        escenario.setTitle(titulo);
        escenario.setScene(escena);
        escenario.sizeToScene();
        escenario.centerOnScreen();
    }
}
