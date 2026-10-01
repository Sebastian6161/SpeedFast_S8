package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaAsignarEntrega extends JFrame {

    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;

    public VentanaAsignarEntrega(
            controladores.PedidoController pedidoController
    ) {

        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregaDAO = new EntregaDAO();

        setTitle("Registrar Entrega");
        setSize(550, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel etiquetaPedido =
                new JLabel("Seleccione el pedido:");

        JLabel etiquetaRepartidor =
                new JLabel("Seleccione el repartidor:");

        comboPedidos = new JComboBox<>();
        comboRepartidores = new JComboBox<>();

        JButton botonRefrescar =
                new JButton("Refrescar datos");

        JButton botonRegistrar =
                new JButton("Registrar Entrega");

        add(etiquetaPedido);
        add(comboPedidos);

        add(etiquetaRepartidor);
        add(comboRepartidores);

        add(new JLabel(""));
        add(botonRefrescar);

        add(new JLabel(""));
        add(botonRegistrar);

        cargarDatos();

        botonRefrescar.addActionListener(
                e -> cargarDatos()
        );

        botonRegistrar.addActionListener(
                e -> registrarEntrega()
        );
    }

    private void cargarDatos() {

        comboPedidos.removeAllItems();
        comboRepartidores.removeAllItems();

        List<Pedido> pedidos =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {
                comboPedidos.addItem(pedido);
            }
        }

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {
            comboRepartidores.addItem(repartidor);
        }
    }

    private void registrarEntrega() {

        Pedido pedidoSeleccionado =
                (Pedido) comboPedidos.getSelectedItem();

        Repartidor repartidorSeleccionado =
                (Repartidor) comboRepartidores.getSelectedItem();

        if (pedidoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay pedidos pendientes disponibles.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (repartidorSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay repartidores disponibles.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Entrega entrega = new Entrega(
                0,
                pedidoSeleccionado,
                repartidorSeleccionado,
                LocalDate.now(),
                LocalTime.now()
        );

        boolean guardada =
                entregaDAO.guardar(entrega);

        if (!guardada) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        pedidoSeleccionado.setEstado(
                EstadoPedido.EN_REPARTO
        );

        boolean estadoActualizado =
                pedidoDAO.actualizarEstado(
                        pedidoSeleccionado
                );

        if (!estadoActualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "La entrega fue registrada, pero no se pudo actualizar el estado del pedido.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            cargarDatos();
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Entrega registrada correctamente.\n\n"
                        + "Pedido: #" + pedidoSeleccionado.getId()
                        + "\nDirección: "
                        + pedidoSeleccionado.getDireccion()
                        + "\nRepartidor: "
                        + repartidorSeleccionado.getNombre()
                        + "\nEstado: EN_REPARTO",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarDatos();
    }
}