package ejemplo.service;

import ejemplo.dao.PersonaDAO;
import ejemplo.model.Persona;
import java.sql.SQLException;
import java.util.List;

public class PersonaService {
    private final PersonaDAO dao = new PersonaDAO();

    public List<Persona> listar() throws SQLException {
        return dao.listar();
    }

    public void guardar(String nombre, String correo) throws SQLException {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        String correoLimpio = correo == null ? "" : correo.trim();

        if (nombreLimpio.isEmpty()) {
            throw new IllegalArgumentException("Escribe un nombre.");
        }
        if (nombreLimpio.length() > 100) {
            throw new IllegalArgumentException("El nombre admite hasta 100 caracteres.");
        }
        if (correoLimpio.isEmpty()) {
            throw new IllegalArgumentException("Escribe un correo.");
        }
        if (correoLimpio.length() > 150) {
            throw new IllegalArgumentException("El correo admite hasta 150 caracteres.");
        }

        // Validación básica para este ejemplo educativo.
        int arroba = correoLimpio.indexOf('@');
        if (arroba <= 0 || arroba == correoLimpio.length() - 1
                || arroba != correoLimpio.lastIndexOf('@')
                || correoLimpio.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("Escribe un correo válido, por ejemplo ana@ejemplo.cl.");
        }

        dao.guardar(nombreLimpio, correoLimpio);
    }
}