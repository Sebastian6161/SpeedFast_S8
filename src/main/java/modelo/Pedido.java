package modelo;

public class Pedido {

    private int id;
    private String direccion;
    private String tipo;
    private EstadoPedido estado;
    private Repartidor repartidor;

    public Pedido(
            int id,
            String direccion,
            String tipo
    ) {

        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;

        this.estado =
                EstadoPedido.PENDIENTE;

        this.repartidor = null;
    }

    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setEstado(
            EstadoPedido estado
    ) {
        this.estado = estado;
    }

    public void asignarRepartidor(
            Repartidor repartidor
    ) {

        this.repartidor = repartidor;

        this.estado =
                EstadoPedido.EN_REPARTO;
    }

    public void entregar() {

        this.estado =
                EstadoPedido.ENTREGADO;
    }

    public void interrumpir() {

        this.estado =
                EstadoPedido.INTERRUMPIDO;
    }

    @Override
    public String toString() {

        return "Pedido #" + id
                + " | Dirección: "
                + direccion
                + " | Tipo: "
                + tipo
                + " | Estado: "
                + estado;
    }
}