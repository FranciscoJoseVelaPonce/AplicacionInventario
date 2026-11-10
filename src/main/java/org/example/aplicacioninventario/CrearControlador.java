package org.example.aplicacioninventario;

import cesur.dam.g1.data.DataSourceFactory;
import cesur.dam.g1.data.EquipoJdbcDAO;
import cesur.dam.g1.models.Equipo;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CrearControlador {

    private static final String ESTILO_ERROR = "-fx-border-color: red; -fx-border-width: 2px;";
    private static final String DISPONIBLE = "Disponible";
    private static final String NO_DISPONIBLE = "No disponible";
    private static final int MAX_NOMBRE = 80;
    private static final int MAX_DESCRIPCION = 255;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ChoiceBox<String> cmbCategoria;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private Button btnGuardar;

    private EquipoJdbcDAO dao;

    @FXML
    private void initialize() {
        cmbEstado.getItems().addAll(DISPONIBLE, NO_DISPONIBLE);
        dpFecha.setConverter(new LocalDateStringConverter(DateTimeFormatter.ofPattern("dd/MM/yyyy"), null));

        btnGuardar.disableProperty().bind(Bindings.createBooleanBinding(
                () -> txtNombre.getText().isBlank() || cmbCategoria.getValue() == null || cmbEstado.getValue() == null,
                txtNombre.textProperty(), cmbCategoria.valueProperty(), cmbEstado.valueProperty()
        ));

        txtNombre.textProperty().addListener((o, a, n) -> txtNombre.setStyle(""));
        txtDescripcion.textProperty().addListener((o, a, n) -> txtDescripcion.setStyle(""));
        dpFecha.valueProperty().addListener((o, a, n) -> dpFecha.setStyle(""));
        cmbCategoria.valueProperty().addListener((o, a, n) -> cmbCategoria.setStyle(""));
        cmbEstado.valueProperty().addListener((o, a, n) -> cmbEstado.setStyle(""));

        try {
            dao = new EquipoJdbcDAO(DataSourceFactory.getDataSource());
            cmbCategoria.getItems().addAll(
                    dao.findAll().stream()
                            .map(Equipo::getCategoria)
                            .filter(c -> c != null && !c.isBlank())
                            .distinct()
                            .sorted()
                            .toList()
            );
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo conectar con la base de datos:\n" + e.getMessage());
        }
    }

    @FXML
    private void onGuardar() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        LocalDate fecha = leerFecha();
        String categoria = cmbCategoria.getValue();
        String estado = cmbEstado.getValue();

        StringBuilder errores = new StringBuilder();

        if (nombre.isEmpty()) {
            agregarError(errores, txtNombre, "El nombre no puede estar vacío.");
        } else if (nombre.length() < 2) {
            agregarError(errores, txtNombre, "El nombre debe tener al menos 2 caracteres.");
        } else if (nombre.length() > MAX_NOMBRE) {
            agregarError(errores, txtNombre, "El nombre no puede superar " + MAX_NOMBRE + " caracteres.");
        } else if (!nombre.matches(".*\\p{L}.*")) {
            agregarError(errores, txtNombre, "El nombre debe contener alguna letra.");
        }

        if (descripcion.length() > MAX_DESCRIPCION) {
            agregarError(errores, txtDescripcion, "La descripción no puede superar " + MAX_DESCRIPCION + " caracteres.");
        }

        if (fecha == null) {
            agregarError(errores, dpFecha, "Tienes que indicar una fecha válida.");
        } else if (fecha.isAfter(LocalDate.now())) {
            agregarError(errores, dpFecha, "La fecha no puede ser posterior a hoy.");
        }

        if (categoria == null) {
            agregarError(errores, cmbCategoria, "Tienes que seleccionar una categoría.");
        }

        if (estado == null) {
            agregarError(errores, cmbEstado, "Tienes que seleccionar un estado.");
        }

        if (!errores.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "No se puede guardar", errores.toString());
            return;
        }

        Equipo equipo = new Equipo();
        equipo.setNombre(nombre);
        equipo.setDescripcion(descripcion.isEmpty() ? null : descripcion);
        equipo.setFecha(fecha);
        equipo.setCategoria(categoria);
        equipo.setDisponible(DISPONIBLE.equals(estado));

        try {
            dao.save(equipo);
        } catch (RuntimeException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el equipo:\n" + e.getMessage());
            return;
        }

        mostrarAlerta(Alert.AlertType.INFORMATION, "Equipo guardado", equipo.getNombre() + " (ID " + equipo.getId() + ")");
        NavegacionControlador.navegarA("principal-vista.fxml", "Inventario");
    }

    @FXML
    private void onCancelar() {
        NavegacionControlador.navegarA("principal-vista.fxml", "Inventario");
    }

    private LocalDate leerFecha() {
        String texto = dpFecha.getEditor().getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            LocalDate fecha = dpFecha.getConverter().fromString(texto);
            dpFecha.setValue(fecha);
            return fecha;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private void agregarError(StringBuilder errores, Control campo, String mensaje) {
        errores.append("• ").append(mensaje).append("\n");
        campo.setStyle(ESTILO_ERROR);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
