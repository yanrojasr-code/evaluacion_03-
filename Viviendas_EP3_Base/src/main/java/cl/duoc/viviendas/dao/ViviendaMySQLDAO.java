package cl.duoc.viviendas.dao;
import cl.duoc.viviendas.conexion.ConexionBD;
import cl.duoc.viviendas.model.Vivienda;
import cl.duoc.viviendas.model.Casa;
import cl.duoc.viviendas.model.Departamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViviendaMySQLDAO implements ViviendaDAO {
    @Override
    public List<Vivienda> obtenerTodos() throws PersistenciaException {

        List<Vivienda> viviendas = new ArrayList<>();

        String sql = """
            
                SELECT v.id, tv.descripcion, v.codigo,
                   v.superficie, v.habitaciones, v.piso,
                   v.gasto_comun_al_dia, v.estacionamiento, v.patio
            FROM vivienda v
            JOIN tipo_vivienda tv ON v.id_tipo = tv.id_tipo
   

           """;

        try (Connection conexion = ConexionBD.getConnection
                ();
             PreparedStatement ps = conexion.
                     prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String tipo = rs.
                        getString("descripcion");
                String codigo = rs.getString("codigo");
                double
                        superficie = rs.getDouble("superficie");
                int habitaciones = rs.getInt(

                        "habitaciones");
                Vivienda vivienda;

                if ("Departamento".equals
                        (tipo)) {
                    vivienda = new
                            Departamento(
                            codigo,
                            superficie,
                            habitaciones,
                            rs.getInt("piso"),
                            rs.getBoolean(
                                    "gasto_comun_al_dia"),
                            rs.getBoolean(
                                    "estacionamiento")
                    );
                } else {
                    vivienda = new Casa(

                            codigo,
                            superficie,
                            habitaciones,
                            rs.getBoolean("patio")
                    );
                }

                vivienda.setId(rs.getInt("id"));
                viviendas.add(vivienda);
            }
            return viviendas;

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al consultar las viviendas.", e
            );
        }
    }
    @Override
    public void insertar(Vivienda vivienda) throws PersistenciaException {

        String sql = """
            INSERT INTO vivienda
            (id_tipo, codigo, superficie, habitaciones, piso,
             gasto_comun_al_dia, estacionamiento, patio)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            if (vivienda instanceof Departamento d) {
                ps.setInt(1, 1);
                ps.setString(2, d.getCodigo());
                ps.setDouble(3, d.getSuperficie());
                ps.setInt(4, d.getHabitaciones());
                ps.setInt(5, d.getPiso());
                ps.setBoolean(6, d.isGastoComunAlDia());
                ps.setBoolean(7, d.isEstacionamiento());
                ps.setNull(8, java.sql.Types.BOOLEAN);

            } else if (vivienda instanceof Casa c) {
                ps.setInt(1, 2);
                ps.setString(2, c.getCodigo());
                ps.setDouble(3, c.getSuperficie());
                ps.setInt(4, c.getHabitaciones());
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setNull(6, java.sql.Types.BOOLEAN);
                ps.setNull(7, java.sql.Types.BOOLEAN);
                ps.setBoolean(8, c.isPatio());
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    vivienda.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al insertar la vivienda.", e
            );
        }
    }
    @Override
    public void actualizar(Vivienda vivienda) throws PersistenciaException {
        String sql = """
            UPDATE vivienda
            SET codigo = ?, superficie = ?, habitaciones = ?,
                piso = ?, gasto_comun_al_dia = ?,
                estacionamiento = ?, patio = ?
            WHERE id = ?
            """;

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, vivienda.getCodigo());
            ps.setDouble(2, vivienda.getSuperficie());
            ps.setInt(3, vivienda.getHabitaciones());

            if (vivienda instanceof Departamento d) {
                ps.setInt(4, d.getPiso());
                ps.setBoolean(5, d.isGastoComunAlDia());
                ps.setBoolean(6, d.isEstacionamiento());
                ps.setNull(7, java.sql.Types.BOOLEAN);
            } else if (vivienda instanceof Casa c) {
                ps.setNull(4, java.sql.Types.INTEGER);
                ps.setNull(5, java.sql.Types.BOOLEAN);
                ps.setNull(6, java.sql.Types.BOOLEAN);
                ps.setBoolean(7, c.isPatio());
            }

            ps.setInt(8, vivienda.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar la vivienda.", e);
        }
    }
    @Override
    public void eliminar(int id) throws PersistenciaException {
        String sql = "DELETE FROM vivienda WHERE id = ?";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar la vivienda.", e);
        }
    }
}