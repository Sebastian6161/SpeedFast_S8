package main;

import controladores.PedidoController;
import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        PedidoController pedidoController =
                new PedidoController();

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventanaPrincipal =
                    new VentanaPrincipal(pedidoController);

            ventanaPrincipal.setVisible(true);
        });
    }
}
