package vista;

import controladores.PedidoController;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PedidoController pedidoController;

    public VentanaPrincipal(PedidoController pedidoController) {

        this.pedidoController = pedidoController;

        setTitle("SpeedFast - Gestión de Pedidos");
        setSize(450, 400);
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
                new GridLayout(4, 1, 10, 10)
        );

        JButton botonRegistrar =
                new JButton("Registrar pedido");

        JButton botonListar =
                new JButton("Listar pedidos");

        JButton botonEntrega =
                new JButton("Asignar repartidor");

        JButton botonProcesarCola =
                new JButton("Procesar cola de entregas");

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonListar);
        panelBotones.add(botonEntrega);
        panelBotones.add(botonProcesarCola);

        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        botonRegistrar.addActionListener(e ->
                new VentanaRegistroPedido(pedidoController)
                        .setVisible(true)
        );

        botonListar.addActionListener(e ->
                new VentanaListaPedidos(pedidoController)
                        .setVisible(true)
        );

        botonEntrega.addActionListener(e ->
                new VentanaAsignarEntrega(pedidoController)
                        .setVisible(true)
        );

        botonProcesarCola.addActionListener(e -> {
            if (pedidoController.obtenerColaPedidos().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay pedidos pendientes en la cola para procesar.");
            } else {
                pedidoController.procesarCola();
                JOptionPane.showMessageDialog(this, "Se han procesado todas las entregas en la cola.");
            }
        });
    }
}