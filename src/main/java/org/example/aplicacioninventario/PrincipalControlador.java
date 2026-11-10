package org.example.aplicacioninventario;

import cesur.dam.g1.data.DataSourceFactory;
import cesur.dam.g1.data.EquipoJdbcDAO;
import cesur.dam.g1.models.Equipo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrincipalControlador {

    @FXML
    private TableView<Equipo> tblEquipos;
    @FXML
    private TableColumn<Equipo, Long> colId;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, String> colCategoria;
    @FXML
    private TableColumn<Equipo, Boolean> colDisponible;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        colDisponible.setCellFactory(columna -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean disponible, boolean vacia) {
                super.updateItem(disponible, vacia);
                if (vacia || disponible == null) {
                    setText(null);
                } else if (disponible) {
                    setText("Sí");
                } else {
                    setText("No");
                }
            }
        });

        try {
            EquipoJdbcDAO dao = new EquipoJdbcDAO(DataSourceFactory.getDataSource());
            tblEquipos.setItems(FXCollections.observableArrayList(dao.findAll()));
        } catch (RuntimeException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se pudieron cargar los equipos");
            alerta.setContentText(e.getMessage());
            alerta.show();
        }
    }

    @FXML
    private void onNuevo() {
        NavegacionControlador.navegarA("crear-vista.fxml", "Inventario - Nuevo equipo");
    }

    @FXML
    private void onCerrarSesion() {
        NavegacionControlador.navegarA("login-vista.fxml", "Inventario - Login");
    }
}
