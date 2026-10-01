package vista;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private PedidoDAO pedidoDAO;

    private DefaultTableModel modeloTabla;
    private JTable tablaPedidos;

    private JTextField campoDireccion;
    private JComboBox<String> comboTipo;
    private JComboBox<EstadoPedido> comboEstado;

    public VentanaListaPedidos() {

        this.pedidoDAO = new PedidoDAO();

        setTitle("Gestión de Pedidos");
        setSize(850, 500);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        iniciarFormulario();
        iniciarTabla();
        iniciarBotones();

        actualizarTabla();
    }

    private void iniciarFormulario() {

        JPanel panelFormulario =
                new JPanel(new GridLayout(2, 3, 10, 5));

        panelFormulario.add(
                new JLabel("Dirección:")
        );

        panelFormulario.add(
                new JLabel("Tipo:")
        );

        panelFormulario.add(
                new JLabel("Estado:")
        );

        campoDireccion = new JTextField();

        comboTipo = new JComboBox<>(
                new String[]{
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                }
        );

        comboEstado = new JComboBox<>(
                new EstadoPedido[]{
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_REPARTO,
                        EstadoPedido.ENTREGADO
                }
        );

        panelFormulario.add(campoDireccion);
        panelFormulario.add(comboTipo);
        panelFormulario.add(comboEstado);

        add(
                panelFormulario,
                BorderLayout.NORTH
        );
    }

    private void iniciarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Dirección",
                        "Tipo",
                        "Estado",
                        "Repartidor"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaPedidos.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarPedidoSeleccionado();
                    }
                });

        JScrollPane scrollPane =
                new JScrollPane(tablaPedidos);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    private void iniciarBotones() {

        JPanel panelBotones =
                new JPanel(new FlowLayout());

        JButton botonActualizar =
                new JButton("Actualizar datos");

        JButton botonEliminar =
                new JButton("Eliminar");

        JButton botonRecargar =
                new JButton("Recargar tabla");

        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonRecargar);

        botonActualizar.addActionListener(
                e -> actualizarPedido()
        );

        botonEliminar.addActionListener(
                e -> eliminarPedido()
        );

        botonRecargar.addActionListener(
                e -> actualizarTabla()
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {

            String nombreRepartidor;

            if (pedido.getRepartidor() == null) {
                nombreRepartidor = "Sin asignar";
            } else {
                nombreRepartidor =
                        pedido.getRepartidor().getNombre();
            }

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccion(),
                            pedido.getTipo(),
                            pedido.getEstado(),
                            nombreRepartidor
                    }
            );
        }
    }

    private void cargarPedidoSeleccionado() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        campoDireccion.setText(
                modeloTabla
                        .getValueAt(fila, 1)
                        .toString()
        );

        comboTipo.setSelectedItem(
                modeloTabla
                        .getValueAt(fila, 2)
                        .toString()
        );

        String estado =
                modeloTabla
                        .getValueAt(fila, 3)
                        .toString();

        comboEstado.setSelectedItem(
                EstadoPedido.valueOf(estado)
        );
    }

    private void actualizarPedido() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String direccion =
                campoDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección no puede estar vacía.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        String tipo =
                (String) comboTipo.getSelectedItem();

        EstadoPedido estado =
                (EstadoPedido) comboEstado.getSelectedItem();

        Pedido pedido =
                new Pedido(
                        id,
                        direccion,
                        tipo
                );

        pedido.setEstado(estado);

        boolean actualizado =
                pedidoDAO.actualizar(pedido);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente."
            );

            actualizarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarPedido() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el pedido seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                pedidoDAO.eliminar(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente."
            );

            actualizarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el pedido.\n"
                            + "Verifique que no tenga una entrega asociada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarFormulario() {

        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        tablaPedidos.clearSelection();
    }
}