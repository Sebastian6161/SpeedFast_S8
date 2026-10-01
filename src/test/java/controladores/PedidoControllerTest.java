package controladores;

import modelo.EstadoPedido;
import modelo.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoControllerTest {

    private PedidoController controller;

    @BeforeEach
    void setUp() {
        controller = new PedidoController();
    }

    @Test
    @DisplayName("Agregar un pedido correctamente")
    void testAgregarPedido() {

        Pedido pedido = new Pedido(
                1,
                "Av. Providencia 1234",
                "COMIDA"
        );

        boolean agregado =
                controller.agregarPedido(pedido);

        assertTrue(agregado);

        assertEquals(
                1,
                controller.obtenerPedidos().size()
        );

        assertEquals(
                EstadoPedido.PENDIENTE,
                controller.obtenerPedidos()
                        .get(0)
                        .getEstado()
        );
    }

    @Test
    @DisplayName("No permitir pedidos con ID duplicado")
    void testPedidoDuplicado() {

        Pedido pedido1 = new Pedido(
                1,
                "Av. Providencia 1234",
                "COMIDA"
        );

        Pedido pedido2 = new Pedido(
                1,
                "Av. Irarrázaval 1500",
                "EXPRESS"
        );

        assertTrue(
                controller.agregarPedido(pedido1)
        );

        assertFalse(
                controller.agregarPedido(pedido2)
        );

        assertEquals(
                1,
                controller.obtenerPedidos().size()
        );
    }

    @Test
    @DisplayName("La lista de pedidos comienza vacía")
    void testListaInicialVacia() {

        assertTrue(
                controller.obtenerPedidos().isEmpty()
        );
    }

    @Test
    @DisplayName("Agregar varios pedidos")
    void testAgregarVariosPedidos() {

        controller.agregarPedido(
                new Pedido(
                        1,
                        "Dirección 1",
                        "COMIDA"
                )
        );

        controller.agregarPedido(
                new Pedido(
                        2,
                        "Dirección 2",
                        "ENCOMIENDA"
                )
        );

        controller.agregarPedido(
                new Pedido(
                        3,
                        "Dirección 3",
                        "EXPRESS"
                )
        );

        assertEquals(
                3,
                controller.obtenerPedidos().size()
        );
    }
}