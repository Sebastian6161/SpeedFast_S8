package dao;

import modelo.Entrega;

import java.sql.*;

public class EntregaDAO {

    public boolean guardar(Entrega entrega) {

        String sql =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        Connection conexion = null;
        PreparedStatement statement = null;

        try {

            conexion = ConexionBD.conectar();

            statement = conexion.prepareStatement(sql);

            statement.setInt(
                    1,
                    entrega.getPedido().getId()
            );

            statement.setInt(
                    2,
                    entrega.getRepartidor().getId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );

            statement.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al guardar la entrega.");
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
}