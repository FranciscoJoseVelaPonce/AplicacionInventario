package org.example.aplicacioninventario;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControlador {

    private static final String USUARIO = "admin";
    private static final String PASSWORD = "admin";

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        lblError.setText("");
        txtUsuario.textProperty().addListener((o, a, n) -> lblError.setText(""));
    }

    @FXML
    private void onEntrar() {
        if (USUARIO.equals(txtUsuario.getText().trim()) && PASSWORD.equals(txtPassword.getText())) {
            NavegacionControlador.navegarA("principal-vista.fxml", "Inventario");
        } else {
            txtPassword.clear();
            lblError.setText("Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }
}
