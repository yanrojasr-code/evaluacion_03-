package ejemplo.controller;

import ejemplo.model.Persona;
import ejemplo.service.PersonaService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

public class PersonaController {
    @FXML private TextField nombreField;
    @FXML private TextField correoField;
    @FXML private TableView<Persona> personasTable;
    @FXML private TableColumn<Persona, Long> idColumn;
    @FXML private TableColumn<Persona, String> nombreColumn;
    @FXML private TableColumn<Persona, String> correoColumn;
    @FXML private Label estadoLabel;

    private final PersonaService servicio = new PersonaService();

    // FXMLLoader llama este método después de crear los controles del FXML.
    @FXML
    private void initialize() {
        // Cada columna usa el getter correspondiente de Persona:
        // "id" -> getId(), "nombre" -> getNombre(), "correo" -> getCorreo().
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));
        personasTable.setPlaceholder(new Label("No hay personas guardadas."));
    }

    @FXML
    public void cargar() {
        try {
            actualizar();
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    @FXML
    private void guardar() {
        try {
            String nombre = nombreField.getText();
            String correo = correoField.getText();
            servicio.guardar(nombre, correo);
            nombreField.clear();
            correoField.clear();
            actualizar();
            estadoLabel.setText("Persona guardada.");
            nombreField.requestFocus();
        } catch (SQLException | IllegalArgumentException e) {
            mostrarError(e);
        }
    }

    private void actualizar() throws SQLException {
        personasTable.getItems().setAll(servicio.listar());
        estadoLabel.setText(personasTable.getItems().size() + " persona(s).");
    }

    private void mostrarError(Exception e) {
        estadoLabel.setText("No se pudo completar la operación.");
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}