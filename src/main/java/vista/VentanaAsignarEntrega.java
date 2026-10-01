package vista;

import controladores.PedidoController;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaAsignarEntrega extends JFrame {

    private PedidoController pedidoController;
    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    public VentanaAsignarEntrega(PedidoController pedidoController) {

        this.pedidoController = pedidoController;

        setTitle("Asignar Repartidor");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel etiquetaPedido = new JLabel("Seleccione el pedido:");
        JLabel etiquetaRepartidor = new JLabel("Seleccione el repartidor:");

        comboPedidos = new JComboBox<>();
        comboRepartidores = new JComboBox<>();

        cargarPedidos();
        cargarRepartidores();

        JButton botonAsignar = new JButton("Asignar Repartidor");

        add(etiquetaPedido);
        add(comboPedidos);

        add(etiquetaRepartidor);
        add(comboRepartidores);

        add(new JLabel());
        add(botonAsignar);

        botonAsignar.addActionListener(e -> iniciarEntrega());
    }

    private void cargarPedidos() {

        List<Pedido> pedidos = pedidoController.obtenerPedidos();

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado().toString().equals("PENDIENTE")) {
                comboPedidos.addItem(pedido);
            }
        }
    }

    private void cargarRepartidores() {

        List<Repartidor> repartidores =
                pedidoController.obtenerRepartidores();

        for (Repartidor repartidor : repartidores) {
            comboRepartidores.addItem(repartidor);
        }
    }

    private void iniciarEntrega() {

        Pedido pedidoSeleccionado =
                (Pedido) comboPedidos.getSelectedItem();

        Repartidor repartidorSeleccionado =
                (Repartidor) comboRepartidores.getSelectedItem();

        if (pedidoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay pedidos pendientes."
            );
            return;
        }

        pedidoSeleccionado.asignarRepartidor(repartidorSeleccionado);

        JOptionPane.showMessageDialog(
                this,
                "Repartidor asignado correctamente.\n\n"
                        + "Pedido: #" + pedidoSeleccionado.getId()
                        + "\n"
                        + "Dirección: "
                        + pedidoSeleccionado.getDireccion()
                        + "\n"
                        + "Repartidor: "
                        + repartidorSeleccionado.getNombre()
                        + "\n"
                        + "Estado: EN REPARTO\n\n"
                        + "(Procese la cola desde el menú principal para finalizar a ENTREGADO)"
        );

        dispose();
    }
}
