package cl.duoc.viviendas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Reúne los datos de conexión a la base de datos y entrega conexiones.
 * Ninguna otra clase debe contener estos datos.
 */
public class ConexionBD {

    // =====================================================================
    // EP3: completa los datos de conexión de tu laboratorio.
    //
    //   Formato de URL según el motor:
    //     MySQL  -> jdbc:mysql://<servidor>:<puerto>/<base_de_datos>
    //     Oracle -> jdbc:oracle:thin:@<servidor>:<puerto>/<servicio>
    // =====================================================================
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/viviendas";
    private static final String USUARIO = "root";
    private static final String CLAVE = "Sarai.12";

    private ConexionBD() {
    }

    /**
     * @return una conexión abierta a la base de datos. Quien la utiliza debe cerrarla.
     * @throws SQLException si no es posible conectarse.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
