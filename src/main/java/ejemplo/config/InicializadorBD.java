package ejemplo.config;
import java.sql.*;
public final class InicializadorBD {
 private InicializadorBD() {}
 public static void inicializar() throws SQLException {
  try(Connection cn=Conexion.obtener(); Statement st=cn.createStatement()) {
   st.executeUpdate("CREATE TABLE IF NOT EXISTS personas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL CHECK(length(nombre) BETWEEN 1 AND 100), correo TEXT NOT NULL CHECK(length(correo) BETWEEN 1 AND 150))");
  }
 }
}
