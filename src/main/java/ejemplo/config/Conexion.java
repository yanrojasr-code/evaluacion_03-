package ejemplo.config;
import java.sql.*;
public final class Conexion {
 private Conexion() {}
 public static Connection obtener() throws SQLException {
  return DriverManager.getConnection("jdbc:sqlite:personas.db");
 }
}
