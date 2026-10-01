package dao;

import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    // CREATE
    public boolean guardar(Entrega entrega) {

        String sql = """
                INSERT INTO entregas
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getPedido().getId());
            ps.setInt(2, entrega.getRepartidor().getId());
            ps.setDate(3, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(4, java.sql.Time.valueOf(entrega.getHora()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al guardar entrega: " + e.getMessage()
            );
            return false;
        }
    }

    // READ
    public List<Entrega> listarTodos() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = """
                SELECT e.id,
                       e.fecha,
                       e.hora,
                       p.id AS pedido_id,
                       p.direccion,
                       p.tipo,
                       r.id AS repartidor_id,
                       r.nombre
                FROM entregas e
                INNER JOIN pedidos p
                    ON e.id_pedido = p.id
                INNER JOIN repartidores r
                    ON e.id_repartidor = r.id
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pedido pedido = new Pedido(
                        rs.getInt("pedido_id"),
                        rs.getString("direccion"),
                        rs.getString("tipo")
                );

                Repartidor repartidor = new Repartidor(
                        rs.getInt("repartidor_id"),
                        rs.getString("nombre")
                );

                Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        pedido,
                        repartidor,
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );

                entregas.add(entrega);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar entregas: " + e.getMessage()
            );
        }

        return entregas;
    }

    // UPDATE
    public boolean actualizar(Entrega entrega) {

        String sql = """
                UPDATE entregas
                SET id_pedido = ?,
                    id_repartidor = ?,
                    fecha = ?,
                    hora = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getPedido().getId());
            ps.setInt(2, entrega.getRepartidor().getId());
            ps.setDate(3, java.sql.Date.valueOf(entrega.getFecha()));
            ps.setTime(4, java.sql.Time.valueOf(entrega.getHora()));
            ps.setInt(5, entrega.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar entrega: " + e.getMessage()
            );
            return false;
        }
    }

    // DELETE
    public boolean eliminar(int id) {

        String sql = "DELETE FROM entregas WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al eliminar entrega: " + e.getMessage()
            );
            return false;
        }
    }
}