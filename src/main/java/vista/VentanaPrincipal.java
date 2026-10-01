package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {

        setTitle("SpeedFast - Sistema de Gestión");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "SPEEDFAST - SISTEMA DE GESTIÓN",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(
                new GridLayout(5, 1, 10, 10)
        );

        JButton botonRegistrar =
                new JButton("Registrar pedido");

        JButton botonPedidos =
                new JButton("Gestionar pedidos");

        JButton botonRepartidores =
                new JButton("Gestionar repartidores");

        JButton botonRegistrarEntrega =
                new JButton("Registrar entrega");

        JButton botonGestionarEntregas =
                new JButton("Gestionar entregas");

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonPedidos);
        panelBotones.add(botonRepartidores);
        panelBotones.add(botonRegistrarEntrega);
        panelBotones.add(botonGestionarEntregas);

        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        // Registrar pedido
        botonRegistrar.addActionListener(e ->
                new VentanaRegistroPedido()
                        .setVisible(true)
        );

        // Gestionar pedidos
        botonPedidos.addActionListener(e ->
                new VentanaListaPedidos()
                        .setVisible(true)
        );

        // Gestionar repartidores
        botonRepartidores.addActionListener(e ->
                new VentanaRepartidores()
        );

        // Registrar entrega
        botonRegistrarEntrega.addActionListener(e ->
                new VentanaAsignarEntrega()
                        .setVisible(true)
        );

        // Gestionar entregas
        botonGestionarEntregas.addActionListener(e ->
                new VentanaEntregas()
        );
    }
}