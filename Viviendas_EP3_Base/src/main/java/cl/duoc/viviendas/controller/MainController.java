package cl.duoc.viviendas.controller;

import cl.duoc.viviendas.dao.ViviendaDAO;
import cl.duoc.viviendas.dao.PersistenciaException;
import cl.duoc.viviendas.dao.ViviendaMySQLDAO;
import cl.duoc.viviendas.model.Vivienda;
import cl.duoc.viviendas.model.Departamento;
import cl.duoc.viviendas.model.Casa;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import cl.duoc.viviendas.dao.ViviendaMySQLDAO;
import java.util.Locale;

/**
 * Controlador de la ventana principal: formulario, tabla y área de mensajes.
 * Accede a los datos únicamente a través de la interfaz ViviendaDAO.
 */
public class MainController {

    // =====================================================================
    // EP3: asigna aquí tu implementación de ViviendaDAO
    //      (por ejemplo: = new MiImplementacionDAO();).
    //      No es necesario modificar ninguna otra parte de esta clase.
    // =====================================================================
    private final ViviendaDAO dao = new ViviendaMySQLDAO();

    private static final Locale LOCALE_CL = Locale.forLanguageTag("es-CL");

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtHabitaciones;
    @FXML private TextField txtPiso;
    @FXML private CheckBox chkGastoComunAlDia;
    @FXML private CheckBox chkEstacionamiento;
    @FXML private CheckBox chkPatio;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;

    @FXML private TableView<Vivienda> tabla;
    @FXML private TableColumn<Vivienda, Integer> colId;
    @FXML private TableColumn<Vivienda, String> colTipo;
    @FXML private TableColumn<Vivienda, String> colCodigo;
    @FXML private TableColumn<Vivienda, Double> colSuperficie;
    @FXML private TableColumn<Vivienda, Integer> colHabitaciones;
    @FXML private TableColumn<Vivienda, String> colCosto;

    @FXML private Label lblMensaje;

    /** Registro que se está editando; null cuando el formulario registra uno nuevo. */
    private Vivienda enEdicion;

    @FXML
    private void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList(Departamento.TIPO, Casa.TIPO));
        cbTipo.valueProperty().addListener((obs, anterior, actual) -> actualizarCamposPorTipo());

        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colSuperficie.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getSuperficie()));
        colHabitaciones.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getHabitaciones()));
        colCosto.setCellValueFactory(c ->
                new SimpleStringProperty(formatearCosto(c.getValue().calcularCostoArriendo())));
        tabla.setPlaceholder(new Label("No hay viviendas para mostrar."));

        limpiarFormulario();
        cargarTabla();
    }

    // ---------------------------------------------------------------- acciones

    @FXML
    private void onRegistrar() {
        if (!hayDao()) {
            return;
        }
        Vivienda vivienda;
        try {
            vivienda = crearDesdeFormulario();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
            return;
        }

        try {
            if (enEdicion == null) {
                dao.insertar(vivienda);
                cargarTabla();
                limpiarFormulario();
                mostrarExito(describir(vivienda) + " registrada correctamente.");
            } else {
                vivienda.setId(enEdicion.getId());
                dao.actualizar(vivienda);
                cargarTabla();
                limpiarFormulario();
                mostrarExito(describir(vivienda) + " actualizada correctamente.");
            }
        } catch (PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditar() {
        Vivienda seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una vivienda de la tabla para editar.");
            return;
        }
        enEdicion = seleccionada;
        cbTipo.setValue(seleccionada.getTipo());
        cbTipo.setDisable(true);
        txtCodigo.setText(seleccionada.getCodigo());
        txtSuperficie.setText(String.valueOf(seleccionada.getSuperficie()));
        txtHabitaciones.setText(String.valueOf(seleccionada.getHabitaciones()));
        if (seleccionada instanceof Departamento departamento) {
            txtPiso.setText(String.valueOf(departamento.getPiso()));
            chkGastoComunAlDia.setSelected(departamento.isGastoComunAlDia());
            chkEstacionamiento.setSelected(departamento.isEstacionamiento());
        } else if (seleccionada instanceof Casa casa) {
            chkPatio.setSelected(casa.isPatio());
        }
        btnRegistrar.setText("Guardar cambios");
        btnCancelar.setDisable(false);
        txtCodigo.requestFocus();
        mostrarInfo("Editando " + describir(seleccionada)
                + ". Modifica los datos y presiona Guardar cambios.");
    }

    @FXML
    private void onCancelar() {
        limpiarFormulario();
        mostrarInfo("Edición cancelada.");
    }

    @FXML
    private void onEliminar() {
        if (!hayDao()) {
            return;
        }
        Vivienda seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una vivienda de la tabla para eliminar.");
            return;
        }
        try {
            dao.eliminar(seleccionada.getId());
            if (enEdicion != null && enEdicion.getId() == seleccionada.getId()) {
                limpiarFormulario();
            }
            cargarTabla();
            mostrarExito(describir(seleccionada) + " eliminada correctamente.");
        } catch (PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    // ---------------------------------------------------------------- apoyo

    /** Vuelve a leer todos los registros desde el almacenamiento y los muestra en la tabla. */
    private void cargarTabla() {
        if (!hayDao()) {
            tabla.getItems().clear();
            return;
        }
        try {
            tabla.setItems(FXCollections.observableArrayList(dao.obtenerTodos()));
        } catch (PersistenciaException e) {
            tabla.getItems().clear();
            mostrarError("Error al leer la información: " + e.getMessage());
        }
    }

    private boolean hayDao() {
        if (dao == null) {
            mostrarError("Aún no hay una implementación de ViviendaDAO asignada en MainController.");
            return false;
        }
        return true;
    }

    private Vivienda crearDesdeFormulario() {
        String tipo = cbTipo.getValue();
        if (tipo == null) {
            throw new IllegalArgumentException("Selecciona el tipo.");
        }
        String codigo = txtCodigo.getText();
        double superficie = leerDecimal(txtSuperficie.getText(), "Superficie: debe ser un número.");
        int habitaciones = leerEntero(txtHabitaciones.getText(), "Habitaciones: debe ser un número entero.");

        if (Departamento.TIPO.equals(tipo)) {
            return new Departamento(codigo, superficie, habitaciones, leerEntero(txtPiso.getText(), "Piso: debe ser un número entero."), chkGastoComunAlDia.isSelected(), chkEstacionamiento.isSelected());
        }
        return new Casa(codigo, superficie, habitaciones, chkPatio.isSelected());
    }

    private int leerEntero(String texto, String mensajeError) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private double leerDecimal(String texto, String mensajeError) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private void actualizarCamposPorTipo() {
        boolean esTipo1 = Departamento.TIPO.equals(cbTipo.getValue());
        boolean esTipo2 = Casa.TIPO.equals(cbTipo.getValue());
        txtPiso.setDisable(!esTipo1);
        chkGastoComunAlDia.setDisable(!esTipo1);
        chkEstacionamiento.setDisable(!esTipo1);
        chkPatio.setDisable(!esTipo2);
    }

    private void limpiarFormulario() {
        enEdicion = null;
        cbTipo.setDisable(false);
        cbTipo.setValue(null);
        txtCodigo.clear();
        txtSuperficie.clear();
        txtHabitaciones.clear();
        txtPiso.clear();
        chkGastoComunAlDia.setSelected(false);
        chkEstacionamiento.setSelected(false);
        chkPatio.setSelected(false);
        btnRegistrar.setText("Registrar");
        btnCancelar.setDisable(true);
        actualizarCamposPorTipo();
        tabla.getSelectionModel().clearSelection();
    }

    private String describir(Vivienda registro) {
        return registro.getCodigo() + " (" + registro.getTipo() + ")";
    }

    private String formatearCosto(double costo) {
        return String.format(LOCALE_CL, "$%,.0f", costo);
    }

    private void mostrarExito(String mensaje) {
        mostrarMensaje(mensaje, "#eef5ec", "#a9cfa1", "#1e4620");
    }

    private void mostrarInfo(String mensaje) {
        mostrarMensaje(mensaje, "#eef3f8", "#a8c1d9", "#1f3a56");
    }

    private void mostrarError(String mensaje) {
        mostrarMensaje(mensaje, "#fbeeee", "#e0a3a3", "#8a1f1f");
    }

    private void mostrarMensaje(String mensaje, String fondo, String borde, String texto) {
        lblMensaje.setStyle("-fx-padding: 8 10 8 10; -fx-background-color: " + fondo
                + "; -fx-border-color: " + borde + "; -fx-text-fill: " + texto + ";");
        lblMensaje.setText(mensaje);
    }
}
