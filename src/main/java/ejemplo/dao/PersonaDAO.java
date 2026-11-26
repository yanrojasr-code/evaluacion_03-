package ejemplo.dao;

import ejemplo.config.Conexion;
import ejemplo.model.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    public List<Persona> listar() throws SQLException {
        List<Persona> lista = new ArrayList<>();

        // 1. Abrir la conexión antes del try.
        Connection conexion = Conexion.obtener();

        try {
            // 2. Consultar las tres columnas.
            String sql = "SELECT id, nombre, correo FROM personas ORDER BY id";
            PreparedStatement consulta = conexion.prepareStatement(sql);
            ResultSet resultado = consulta.executeQuery();

            // 3. Convertir cada fila en un objeto Persona.
            while (resultado.next()) {
                long id = resultado.getLong("id");
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correo");
                Persona persona = new Persona(id, nombre, correo);
                lista.add(persona);
            }

            // 4. Cerrar el resultado y la consulta.
            resultado.close();
            consulta.close();
        } finally {
            // También se ejecuta si ocurre un error.
            // Cerrar la conexión libera los recursos JDBC que sigan abiertos.
            conexion.close();
        }

        return lista;
    }

    public void guardar(String nombre, String correo) throws SQLException {
        // 1. Abrir la conexión.
        Connection conexion = Conexion.obtener();

        try {
            // 2. Preparar el INSERT con dos parámetros.
            String sql = "INSERT INTO personas(nombre, correo) VALUES (?, ?)";
            PreparedStatement consulta = conexion.prepareStatement(sql);

            // 3. El primer ? es el nombre; el segundo es el correo.
            consulta.setString(1, nombre);
            consulta.setString(2, correo);

            // 4. Ejecutar y cerrar la consulta.
            consulta.executeUpdate();
            consulta.close();
        } finally {
            // 5. Cerrar la conexión aunque ocurra un error.
            conexion.close();
        }
    }
}