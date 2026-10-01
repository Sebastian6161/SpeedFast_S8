package dao;

import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public List<Repartidor> listarTodos() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor";

        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;

        try {
            conexion = ConexionBD.conectar();

            statement = conexion.prepareStatement(sql);

            resultado = statement.executeQuery();

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");

                Repartidor repartidor =
                        new Repartidor(id, nombre);

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println("Error al listar los repartidores.");
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
                System.out.println("Error al cerrar los recursos.");
                System.out.println(e.getMessage());
            }
        }

        return repartidores;
    }
}
