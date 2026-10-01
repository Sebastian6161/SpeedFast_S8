package vista;

import controladores.PedidoController;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PedidoController pedidoController;

    public VentanaPrincipal(PedidoController pedidoController) {

        this.pedidoController = pedidoController;

        setTitle("SpeedFast - Gestión de Pedidos");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "SPEEDFAST - GESTIÓN DE PEDIDOS",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(
                new GridLayout(5, 1, 10, 10)
        );

        JButton botonRegistrar =
                new JButton("Registrar pedido");

        JButton botonListar =
                new JButton("Listar pedidos");

        JButton botonRepartidores =
                new JButton("Gestionar repartidores");

        JButton botonEntrega =
                new JButton("Asignar repartidor");

        JButton botonProcesarCola =
                new JButton("Procesar cola de entregas");

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonListar);
        panelBotones.add(botonRepartidores);
        panelBotones.add(botonEntrega);
        panelBotones.add(botonProcesarCola);

        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        // Registrar pedido
        botonRegistrar.addActionListener(e ->
                new VentanaRegistroPedido(pedidoController)
                        .setVisible(true)
        );

        // Listar pedidos
        botonListar.addActionListener(e ->
                new VentanaListaPedidos(pedidoController)
                        .setVisible(true)
        );

        // Gestionar repartidores
        botonRepartidores.addActionListener(e ->
                new VentanaRepartidores()
        );

        // Asignar repartidor
        botonEntrega.addActionListener(e ->
                new VentanaAsignarEntrega(pedidoController)
                        .setVisible(true)
        );

        // Procesar cola
        botonProcesarCola.addActionListener(e -> {

            if (pedidoController.obtenerColaPedidos().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No hay pedidos pendientes en la cola para procesar."
                );

            } else {

                pedidoController.procesarCola();

                JOptionPane.showMessageDialog(
                        this,
                        "Se han procesado todas las entregas en la cola."
                );
            }
        });
    }
}