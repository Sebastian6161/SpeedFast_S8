package vista;

import controladores.PedidoController;
import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private PedidoController pedidoController;
    private PedidoDAO pedidoDAO;

    private JTextField campoDireccion;
    private JComboBox<String> comboTipo;

    public VentanaRegistroPedido(
            PedidoController pedidoController
    ) {

        this.pedidoController = pedidoController;
        this.pedidoDAO = new PedidoDAO();

        setTitle("Registrar Pedido");
        setSize(450, 230);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
        setLocationRelativeTo(null);

        setLayout(
                new GridLayout(3, 2, 10, 10)
        );

        // Dirección
        add(new JLabel("Dirección:"));

        campoDireccion = new JTextField();
        add(campoDireccion);

        // Tipo
        add(new JLabel("Tipo:"));

        comboTipo = new JComboBox<>(
                new String[]{
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                }
        );

        add(comboTipo);

        // Botón guardar
        JButton botonGuardar =
                new JButton("Guardar Pedido");

        add(new JLabel(""));
        add(botonGuardar);

        botonGuardar.addActionListener(
                e -> guardarPedido()
        );
    }

    private void guardarPedido() {

        String direccion =
                campoDireccion.getText().trim();

        String tipo =
                (String) comboTipo.getSelectedItem();

        // Validación
        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            campoDireccion.requestFocus();
            return;
        }

        /*
         * El ID se envía como 0 solamente para crear
         * el objeto Java.
         *
         * MySQL genera el ID real mediante AUTO_INCREMENT.
         */
        Pedido pedido =
                new Pedido(
                        0,
                        direccion,
                        tipo
                );

        boolean guardadoBD =
                pedidoDAO.guardar(pedido);

        if (!guardadoBD) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        /*
         * Mantenemos el pedido también en el controlador
         * porque todavía existe la lógica de cola heredada
         * de las semanas anteriores.
         */
        pedidoController.agregarPedido(pedido);

        JOptionPane.showMessageDialog(
                this,
                "Pedido registrado correctamente.",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE
        );

        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        campoDireccion.requestFocus();
    }
}