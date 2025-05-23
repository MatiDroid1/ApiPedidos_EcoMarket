package cl.ecomarket.pedidos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "PEDIDOS")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEDIDO_ID")
    private Long pedidoId;

    @Column(name = "CLIENTE_ID", nullable = false)
    private Long clienteId;
    
    @Column(name = "TIENDA_ID", nullable = false)
    private Long tiendaId;

    @Column(name = "EMPLEADO_ID", nullable = false)
    private Long empleadoId;

    @Column(name = "FECHA_PEDIDO", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @Column(name = "SUBTOTAL", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "DESCUENTO", nullable = false)
    private BigDecimal descuento;

    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    @Column(name = "METODO_PAGO", nullable = false)
    private String metodoPago;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetallePedido> detalles;
}