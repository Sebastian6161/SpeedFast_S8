package vista;

import controladores.PedidoController;
import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private PedidoController pedidoController;
    private PedidoDAO pedidoDAO;

    private JTextField campoId;
    private JTextField campoDireccion;
    private JComboBox<String> comboTipo;

    public VentanaRegistroPedido(
            PedidoController pedidoController
    ) {

        this.pedidoController = pedidoController;
        this.pedidoDAO = new PedidoDAO();

        setTitle("Registrar Pedido");
        setSize(450, 280);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
        setLocationRelativeTo(null);

        setLayout(
                new GridLayout(4, 2, 10, 10)
        );

        add(new JLabel("ID:"));

        campoId = new JTextField();
        add(campoId);

        add(new JLabel("Dirección:"));

        campoDireccion = new JTextField();
        add(campoDireccion);

        add(new JLabel("Tipo:"));

        comboTipo = new JComboBox<>(
                new String[]{
                        "comida",
                        "encomienda",
                        "express"
                }
        );

        add(comboTipo);

        JButton botonGuardar =
                new JButton("Guardar Pedido");

        add(new JLabel(""));
        add(botonGuardar);

        botonGuardar.addActionListener(
                e -> guardarPedido()
        );
    }

    private void guardarPedido() {

        String idTexto =
                campoId.getText().trim();

        String direccion =
                campoDireccion.getText().trim();

        String tipo =
                (String) comboTipo.getSelectedItem();

        if (idTexto.isEmpty()
                || direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos."
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser un número."
            );

            return;
        }

        if (id <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser mayor que cero."
            );

            return;
        }

        Pedido pedido =
                new Pedido(
                        id,
                        direccion,
                        tipo
                );

        /*
         * Primero guardamos en MySQL.
         * Si MySQL rechaza el registro,
         * no lo agregamos a memoria.
         */
        boolean guardadoBD =
                pedidoDAO.guardar(pedido);

        if (!guardadoBD) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el pedido.\n" +
                            "Verifique que el ID no exista."
            );

            return;
        }

        boolean agregado =
                pedidoController.agregarPedido(pedido);

        if (!agregado) {

            JOptionPane.showMessageDialog(
                    this,
                    "El pedido fue guardado en la base de datos."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente."
            );
        }

        campoId.setText("");
        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
    }
}