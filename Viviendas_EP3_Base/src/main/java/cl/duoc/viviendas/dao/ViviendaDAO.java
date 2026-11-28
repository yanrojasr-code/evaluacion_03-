package cl.duoc.viviendas.dao;

import cl.duoc.viviendas.model.Vivienda;

import java.util.List;

/**
 * Operaciones de almacenamiento que utiliza la ventana.
 * <p>
 * Si una operación no puede completarse, debe lanzar PersistenciaException con un
 * mensaje que indique qué operación falló; la ventana muestra ese mensaje en el
 * área de mensajes.
 */
public interface ViviendaDAO {

    /**
     * @return todas las viviendas almacenadas, cada uno como objeto de su tipo
     *         (Departamento o Casa), con todos sus datos y su identificador.
     */
    List<Vivienda> obtenerTodos() throws PersistenciaException;

    /**
     * Guarda un registro nuevo con todos sus datos, incluidos los propios de su tipo.
     * El identificador lo genera la base de datos.
     */
    void insertar(Vivienda vivienda) throws PersistenciaException;

    /**
     * Modifica todos los datos del registro que tiene el mismo identificador,
     * incluidos los propios de su tipo.
     */
    void actualizar(Vivienda vivienda) throws PersistenciaException;

    /**
     * Elimina el registro con el identificador indicado.
     */
    void eliminar(int id) throws PersistenciaException;
}
