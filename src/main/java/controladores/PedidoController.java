package controladores;

import modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    private final List<Pedido> pedidos;

    public PedidoController() {
        pedidos = new ArrayList<>();
    }

    public boolean agregarPedido(Pedido pedido) {

        for (Pedido pedidoExistente : pedidos) {

            if (pedidoExistente.getId() == pedido.getId()) {
                return false;
            }
        }

        pedidos.add(pedido);

        return true;
    }

    public List<Pedido> obtenerPedidos() {
        return pedidos;
    }
}