package controladores;

import modelo.EstadoPedido;
import modelo.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PedidoControllerTest {

    private PedidoController controller;

    @BeforeEach
    void setUp() {
        controller = new PedidoController();
    }

    @ParameterizedTest(name = "{0} pedidos con {1} repartidores")
    @CsvSource({
            "1, 1",
            "10, 2",
            "50, 5",
            "100, 10"
    })
    @DisplayName("Prueba repetible con distintas cantidades de pedidos y repartidores")
    void testProcesamientoPedidosRepetible(int cantidadPedidos, int cantidadRepartidores) {
        controller.configurarRepartidores(cantidadRepartidores);

        for (int i = 1; i <= cantidadPedidos; i++) {
            controller.agregarPedido(new Pedido(i, "Calle " + i, "comida"));
        }

        assertEquals(cantidadPedidos, controller.obtenerColaPedidos().size());

        controller.procesarCola();

        assertTrue(controller.obtenerColaPedidos().isEmpty(), "La cola de pedidos debe quedar vacía.");

        for (Pedido p : controller.obtenerPedidos()) {
            assertEquals(EstadoPedido.ENTREGADO, p.getEstado(), "Cada pedido debe finalizar en estado ENTREGADO.");
            assertNotNull(p.getRepartidor(), "El pedido debe tener un repartidor asignado.");
        }
    }

    @Test
    @DisplayName("Validación con cola vacía inicial")
    void testColaVaciaInicial() {
        assertTrue(controller.obtenerColaPedidos().isEmpty(), "La cola inicial debe estar vacía.");

        assertDoesNotThrow(() -> controller.procesarCola(), "El procesamiento con cola vacía no debe lanzar excepciones.");

        assertTrue(controller.obtenerColaPedidos().isEmpty(), "La cola debe permanecer vacía.");
    }

    @Test
    @DisplayName("Validación del estado final tras interrupciones")
    void testProcesamientoConInterrupcion() {
        int totalPedidos = 10;
        int limiteProcesamiento = 4;

        for (int i = 1; i <= totalPedidos; i++) {
            controller.agregarPedido(new Pedido(i, "Direccion " + i, "express"));
        }

        controller.procesarConInterrupcion(limiteProcesamiento);

        assertTrue(controller.obtenerColaPedidos().isEmpty(), "La cola debe quedar vacía tras procesar/interrumpir.");

        long entregados = controller.obtenerPedidos().stream()
                .filter(p -> p.getEstado() == EstadoPedido.ENTREGADO)
                .count();

        long interrumpidos = controller.obtenerPedidos().stream()
                .filter(p -> p.getEstado() == EstadoPedido.INTERRUMPIDO)
                .count();

        assertEquals(limiteProcesamiento, entregados, "Deben haberse entregado los pedidos antes del límite.");
        assertEquals(totalPedidos - limiteProcesamiento, interrumpidos, "Los pedidos restantes deben quedar en estado INTERRUMPIDO.");

        for (Pedido p : controller.obtenerPedidos()) {
            assertTrue(
                    p.getEstado() == EstadoPedido.ENTREGADO || p.getEstado() == EstadoPedido.INTERRUMPIDO,
                    "El estado final de cada pedido debe ser ENTREGADO o INTERRUMPIDO."
            );
        }
    }
}
