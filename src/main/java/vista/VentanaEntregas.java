package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class VentanaEntregas extends JFrame {

    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;

    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;
    private JTextField campoFecha;
    private JTextField campoHora;

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private List<Entrega> entregasActuales;

    public VentanaEntregas() {

        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregasActuales = new ArrayList<>();

        setTitle("Gestión de Entregas");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        iniciarFormulario();
        iniciarTabla();
        iniciarBotones();

        cargarCombos();
        actualizarTabla();

        setVisible(true);
    }

    private void iniciarFormulario() {

        JPanel panelFormulario =
                new JPanel(new GridLayout(2, 4, 10, 5));

        panelFormulario.add(new JLabel("Pedido:"));
        panelFormulario.add(new JLabel("Repartidor:"));
        panelFormulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        panelFormulario.add(new JLabel("Hora (HH:MM):"));

        comboPedidos = new JComboBox<>();
        comboRepartidores = new JComboBox<>();

        campoFecha = new JTextField(
                LocalDate.now().toString()
        );

        campoHora = new JTextField(
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0)
                        .toString()
        );

        panelFormulario.add(comboPedidos);
        panelFormulario.add(comboRepartidores);
        panelFormulario.add(campoFecha);
        panelFormulario.add(campoHora);

        add(panelFormulario, BorderLayout.NORTH);
    }

    private void iniciarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Pedido",
                        "Dirección",
                        "Repartidor",
                        "Fecha",
                        "Hora"
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

        tablaEntregas = new JTable(modeloTabla);

        tablaEntregas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEntregas
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarEntregaSeleccionada();
                    }
                });

        add(
                new JScrollPane(tablaEntregas),
                BorderLayout.CENTER
        );
    }

    private void iniciarBotones() {

        JPanel panelBotones =
                new JPanel(new FlowLayout());

        JButton botonActualizar =
                new JButton("Actualizar");

        JButton botonEliminar =
                new JButton("Eliminar");

        JButton botonRefrescar =
                new JButton("Refrescar");

        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonRefrescar);

        botonActualizar.addActionListener(
                e -> actualizarEntrega()
        );

        botonEliminar.addActionListener(
                e -> eliminarEntrega()
        );

        botonRefrescar.addActionListener(e -> {
            cargarCombos();
            actualizarTabla();
        });

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCombos() {

        comboPedidos.removeAllItems();
        comboRepartidores.removeAllItems();

        List<Pedido> pedidos =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {
            comboPedidos.addItem(pedido);
        }

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {
            comboRepartidores.addItem(repartidor);
        }
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        entregasActuales =
                entregaDAO.listarTodos();

        for (Entrega entrega : entregasActuales) {

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getPedido().getId(),
                            entrega.getPedido()
                                    .getDireccion(),
                            entrega.getRepartidor()
                                    .getNombre(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void cargarEntregaSeleccionada() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1) {
            return;
        }

        Entrega entrega =
                entregasActuales.get(fila);

        seleccionarPedido(
                entrega.getPedido().getId()
        );

        seleccionarRepartidor(
                entrega.getRepartidor().getId()
        );

        campoFecha.setText(
                entrega.getFecha().toString()
        );

        campoHora.setText(
                entrega.getHora()
                        .withNano(0)
                        .toString()
        );
    }

    private void seleccionarPedido(int idPedido) {

        for (int i = 0;
             i < comboPedidos.getItemCount();
             i++) {

            Pedido pedido =
                    comboPedidos.getItemAt(i);

            if (pedido.getId() == idPedido) {
                comboPedidos.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarRepartidor(
            int idRepartidor
    ) {

        for (int i = 0;
             i < comboRepartidores.getItemCount();
             i++) {

            Repartidor repartidor =
                    comboRepartidores.getItemAt(i);

            if (repartidor.getId()
                    == idRepartidor) {

                comboRepartidores
                        .setSelectedIndex(i);

                return;
            }
        }
    }

    private void actualizarEntrega() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Pedido pedido =
                (Pedido) comboPedidos
                        .getSelectedItem();

        Repartidor repartidor =
                (Repartidor) comboRepartidores
                        .getSelectedItem();

        if (pedido == null
                || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido y un repartidor.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(
                            campoFecha.getText().trim()
                    );

            LocalTime hora =
                    LocalTime.parse(
                            campoHora.getText().trim()
                    );

            int idEntrega =
                    entregasActuales
                            .get(fila)
                            .getId();

            Entrega entrega =
                    new Entrega(
                            idEntrega,
                            pedido,
                            repartidor,
                            fecha,
                            hora
                    );

            boolean actualizada =
                    entregaDAO.actualizar(entrega);

            if (actualizada) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente."
                );

                cargarCombos();
                actualizarTabla();
                limpiarSeleccion();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No fue posible actualizar la entrega.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha u hora no tienen un formato válido.\n"
                            + "Fecha: AAAA-MM-DD\n"
                            + "Hora: HH:MM",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void eliminarEntrega() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar la entrega seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int idEntrega =
                entregasActuales
                        .get(fila)
                        .getId();

        boolean eliminada =
                entregaDAO.eliminar(idEntrega);

        if (eliminada) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente."
            );

            cargarCombos();
            actualizarTabla();
            limpiarSeleccion();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarSeleccion() {

        tablaEntregas.clearSelection();

        campoFecha.setText(
                LocalDate.now().toString()
        );

        campoHora.setText(
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0)
                        .toString()
        );
    }
}