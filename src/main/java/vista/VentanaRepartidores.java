package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaRepartidores extends JFrame {

    private JTextField txtNombre;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private final RepartidorDAO repartidorDAO;

    public VentanaRepartidores() {

        repartidorDAO = new RepartidorDAO();

        setTitle("Gestión de Repartidores");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarTabla();

        setVisible(true);
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel panelFormulario = new JPanel(new FlowLayout());

        JLabel lblNombre = new JLabel("Nombre:");

        txtNombre = new JTextField(20);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);

        // Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRepartidores = new JTable(modeloTabla);
        tablaRepartidores.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(tablaRepartidores);

        // Panel inferior
        JPanel panelBotones = new JPanel(new FlowLayout());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> guardarRepartidor());

        btnActualizar.addActionListener(e -> actualizarRepartidor());

        btnEliminar.addActionListener(e -> eliminarRepartidor());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaRepartidores.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarRepartidorSeleccionado();
                    }
                });
    }

    private void guardarRepartidor() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre del repartidor.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(0, nombre);

        boolean guardado =
                repartidorDAO.guardar(repartidor);

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente."
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede estar vacío.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        Repartidor repartidor =
                new Repartidor(id, nombre);

        boolean actualizado =
                repartidorDAO.actualizar(repartidor);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente."
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el repartidor seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                repartidorDAO.eliminar(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente."
            );

            cargarTabla();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {

            modeloTabla.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    private void cargarRepartidorSeleccionado() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila != -1) {

            String nombre =
                    modeloTabla.getValueAt(fila, 1).toString();

            txtNombre.setText(nombre);
        }
    }

    private void limpiarFormulario() {

        txtNombre.setText("");
        tablaRepartidores.clearSelection();
        txtNombre.requestFocus();
    }
}