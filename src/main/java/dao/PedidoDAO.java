package dao;

import modelo.EstadoPedido;
import modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // CREATE
    public boolean guardar(Pedido pedido) {

        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo().toUpperCase());
            ps.setString(3, pedido.getEstado().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar pedido: " + e.getMessage());
            return false;
        }
    }

    // READ
    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT id, direccion, tipo, estado FROM pedidos";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo")
                );

                pedido.setEstado(
                        EstadoPedido.valueOf(rs.getString("estado"))
                );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    // UPDATE
    public boolean actualizar(Pedido pedido) {

        String sql =
                "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo().toUpperCase());
            ps.setString(3, pedido.getEstado().name());
            ps.setInt(4, pedido.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminar(int id) {

        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }

    // ACTUALIZAR SOLO EL ESTADO
    public boolean actualizarEstado(Pedido pedido) {

        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getEstado().name());
            ps.setInt(2, pedido.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar estado del pedido: " + e.getMessage()
            );
            return false;
        }
    }
}