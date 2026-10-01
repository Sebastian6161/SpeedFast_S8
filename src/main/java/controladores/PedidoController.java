package controladores;

import dao.EntregaDAO;
import dao.PedidoDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PedidoController {

    private List<Pedido> pedidos;
    private List<Repartidor> repartidores;
    private Queue<Pedido> colaPedidos;

    private PedidoDAO pedidoDAO;
    private EntregaDAO entregaDAO;

    public PedidoController() {

        pedidos = new ArrayList<>();
        colaPedidos = new LinkedList<>();

        pedidoDAO = new PedidoDAO();
        entregaDAO = new EntregaDAO();

        repartidores = new ArrayList<>();

        repartidores.add(
                new Repartidor(1, "Carlos")
        );

        repartidores.add(
                new Repartidor(2, "María")
        );

        repartidores.add(
                new Repartidor(3, "Pedro")
        );
    }

    public boolean agregarPedido(Pedido pedido) {

        for (Pedido pedidoExistente : pedidos) {

            if (pedidoExistente.getId()
                    == pedido.getId()) {

                return false;
            }
        }

        pedidos.add(pedido);
        colaPedidos.add(pedido);

        return true;
    }

    public List<Pedido> obtenerPedidos() {
        return pedidos;
    }

    public List<Repartidor> obtenerRepartidores() {
        return repartidores;
    }

    public Queue<Pedido> obtenerColaPedidos() {
        return colaPedidos;
    }

    public void configurarRepartidores(
            int cantidad
    ) {

        repartidores.clear();

        for (int i = 1; i <= cantidad; i++) {

            repartidores.add(
                    new Repartidor(
                            i,
                            "Repartidor " + i
                    )
            );
        }
    }

    public void procesarCola() {

        int indexRepartidor = 0;

        while (!colaPedidos.isEmpty()) {

            Pedido pedido =
                    colaPedidos.poll();

            if (pedido != null
                    && !repartidores.isEmpty()) {

                if (pedido.getRepartidor() == null) {

                    Repartidor repartidor =
                            repartidores.get(
                                    indexRepartidor
                                            % repartidores.size()
                            );

                    pedido.asignarRepartidor(
                            repartidor
                    );

                    indexRepartidor++;
                }

                pedido.entregar();

                // Actualiza estado en MySQL
                pedidoDAO.actualizarEstado(
                        pedido
                );

                // Registra la entrega
                registrarEntrega(pedido);
            }
        }
    }

    public void procesarConInterrupcion(
            int limiteAntesDeInterrumpir
    ) {

        int indexRepartidor = 0;
        int procesados = 0;

        while (!colaPedidos.isEmpty()) {

            if (procesados
                    >= limiteAntesDeInterrumpir) {

                while (!colaPedidos.isEmpty()) {

                    Pedido pedidoInterrumpido =
                            colaPedidos.poll();

                    if (pedidoInterrumpido != null) {

                        pedidoInterrumpido.interrumpir();

                        pedidoDAO.actualizarEstado(
                                pedidoInterrumpido
                        );
                    }
                }

                break;
            }

            Pedido pedido =
                    colaPedidos.poll();

            if (pedido != null
                    && !repartidores.isEmpty()) {

                if (pedido.getRepartidor() == null) {

                    Repartidor repartidor =
                            repartidores.get(
                                    indexRepartidor
                                            % repartidores.size()
                            );

                    pedido.asignarRepartidor(
                            repartidor
                    );

                    indexRepartidor++;
                }

                pedido.entregar();

                pedidoDAO.actualizarEstado(
                        pedido
                );

                registrarEntrega(pedido);

                procesados++;
            }
        }
    }

    private void registrarEntrega(
            Pedido pedido
    ) {

        if (pedido.getRepartidor() == null) {
            return;
        }

        Entrega entrega =
                new Entrega(
                        0,
                        pedido,
                        pedido.getRepartidor(),
                        LocalDate.now(),
                        LocalTime.now()
                );

        entregaDAO.guardar(entrega);
    }
}