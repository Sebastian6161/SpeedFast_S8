package dao;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean guardar(Pedido pedido) {

        String sql =
                "INSERT INTO pedido (id, direccion, tipo, estado) " +
                        "VALUES (?, ?, ?, ?)";

        Connection conexion = null;
        PreparedStatement statement = null;

        try {

            conexion = ConexionBD.conectar();
            statement = conexion.prepareStatement(sql);

            statement.setInt(1, pedido.getId());
            statement.setString(2, pedido.getDireccion());
            statement.setString(3, pedido.getTipo());
            statement.setString(4, pedido.getEstado().name());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al guardar el pedido.");
            System.out.println(e.getMessage());

            return false;

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }

                if (conexion != null) {
                    conexion.close();
                }

            } catch (SQLException e) {
                System.out.println("Error al cerrar los recursos.");
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean actualizarEstado(Pedido pedido) {

        String sql =
                "UPDATE pedido SET estado = ? WHERE id = ?";

        Connection conexion = null;
        PreparedStatement statement = null;

        try {

            conexion = ConexionBD.conectar();
            statement = conexion.prepareStatement(sql);

            statement.setString(
                    1,
                    pedido.getEstado().name()
            );

            statement.setInt(
                    2,
                    pedido.getId()
            );

            int filasActualizadas =
                    statement.executeUpdate();

            return filasActualizadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar el estado del pedido."
            );

            System.out.println(e.getMessage());

            return false;

        } finally {

            try {

                if (statement != null) {
                    statement.close();
                }

                if (conexion != null) {
                    conexion.close();
                }

            } catch (SQLException e) {

                System.out.println(
                        "Error al cerrar los recursos."
                );

                System.out.println(e.getMessage());
            }
        }
    }

    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql =
                "SELECT p.id, p.direccion, p.tipo, p.estado, " +
                        "r.id AS repartidor_id, " +
                        "r.nombre AS repartidor_nombre " +
                        "FROM pedido p " +
                        "LEFT JOIN entrega e ON p.id = e.id_pedido " +
                        "LEFT JOIN repartidor r ON e.id_repartidor = r.id " +
                        "ORDER BY p.id";

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;

        try {

            conexion = ConexionBD.conectar();
            statement = conexion.prepareStatement(sql);
            resultado = statement.executeQuery();

            while (resultado.next()) {

                int id =
                        resultado.getInt("id");

                String direccion =
                        resultado.getString("direccion");

                String tipo =
                        resultado.getString("tipo");

                String estadoTexto =
                        resultado.getString("estado");

                Pedido pedido =
                        new Pedido(id, direccion, tipo);

                EstadoPedido estado =
                        EstadoPedido.valueOf(estadoTexto);

                pedido.setEstado(estado);

                int repartidorId =
                        resultado.getInt("repartidor_id");

                String repartidorNombre =
                        resultado.getString(
                                "repartidor_nombre"
                        );

                if (repartidorNombre != null) {

                    Repartidor repartidor =
                            new Repartidor(
                                    repartidorId,
                                    repartidorNombre
                            );

                    pedido.asignarRepartidor(repartidor);

                    // Conserva el estado real almacenado
                    pedido.setEstado(estado);
                }

                pedidos.add(pedido);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar los pedidos."
            );

            System.out.println(e.getMessage());

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (conexion != null) {
                    conexion.close();
                }

            } catch (SQLException e) {

                System.out.println(
                        "Error al cerrar los recursos."
                );

                System.out.println(e.getMessage());
            }
        }

        return pedidos;
    }
}