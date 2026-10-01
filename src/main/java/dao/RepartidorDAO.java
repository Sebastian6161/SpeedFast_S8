package dao;

import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    // CREATE
    public boolean guardar(Repartidor repartidor) {

        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar repartidor: " + e.getMessage());
            return false;
        }
    }

    // READ
    public List<Repartidor> listarTodos() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidores";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Repartidor repartidor = new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar repartidores: " + e.getMessage());
        }

        return repartidores;
    }

    // UPDATE
    public boolean actualizar(Repartidor repartidor) {

        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombre());
            ps.setInt(2, repartidor.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar repartidor: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminar(int id) {

        String sql = "DELETE FROM repartidores WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar repartidor: " + e.getMessage());
            return false;
        }
    }
}