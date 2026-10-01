package vista;

import controladores.PedidoController;
import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private PedidoController pedidoController;
    private PedidoDAO pedidoDAO;

    private DefaultTableModel modeloTabla;
    private JTable tablaPedidos;

    public VentanaListaPedidos(
            PedidoController pedidoController
    ) {

        this.pedidoController =
                pedidoController;

        this.pedidoDAO =
                new PedidoDAO();

        setTitle("Lista de Pedidos");
        setSize(800, 350);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        modeloTabla =
                new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Repartidor");

        tablaPedidos =
                new JTable(modeloTabla);

        JScrollPane scrollPane =
                new JScrollPane(tablaPedidos);

        JButton botonActualizar =
                new JButton("Actualizar");

        botonActualizar.addActionListener(
                e -> actualizarTabla()
        );

        setLayout(
                new BorderLayout(10, 10)
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                botonActualizar,
                BorderLayout.SOUTH
        );

        actualizarTabla();
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {

            String nombreRepartidor;

            if (pedido.getRepartidor() == null) {

                nombreRepartidor =
                        "Sin asignar";

            } else {

                nombreRepartidor =
                        pedido
                                .getRepartidor()
                                .getNombre();
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
}