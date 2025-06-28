package cl.ecomarket.pedidos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "PEDIDOS")
@Data
@Schema(description = "Entidad que representa un pedido del sistema")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEDIDO_ID")
    @Schema(description = "ID único del pedido", example = "1")
    private Long pedidoId;

    @Column(name = "CLIENTE_ID", nullable = false)
    @Schema(description = "ID del cliente que hizo el pedido", example = "101")
    private Long clienteId;

    @Column(name = "TIENDA_ID", nullable = false)
    @Schema(description = "ID de la tienda donde se realizó el pedido", example = "5")
    private Long tiendaId;

    @Column(name = "EMPLEADO_ID", nullable = false)
    @Schema(description = "ID del empleado que gestionó el pedido", example = "12")
    private Long empleadoId;

    @Column(name = "FECHA_PEDIDO", nullable = false)
    @Schema(description = "Fecha y hora en que se realizó el pedido", example = "2025-06-25T15:30:00")
    private LocalDateTime fechaPedido;

    @Column(name = "ESTADO", nullable = false)
    @Schema(description = "Estado actual del pedido", example = "EN_PROCESO")
    private String estado;

    @Column(name = "SUBTOTAL", nullable = false)
    @Schema(description = "Subtotal del pedido", example = "50000.00")
    private BigDecimal subtotal;

    @Column(name = "DESCUENTO", nullable = false)
    @Schema(description = "Descuento aplicado al pedido", example = "5000.00")
    private BigDecimal descuento;

    @Column(name = "TOTAL", nullable = false)
    @Schema(description = "Total a pagar del pedido", example = "45000.00")
    private BigDecimal total;

    @Column(name = "METODO_PAGO", nullable = false)
    @Schema(description = "Método de pago utilizado", example = "TARJETA_CREDITO")
    private String metodoPago;

    @JsonManagedReference
    @Schema(description = "Lista de detalles del pedido")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
private List<DetallePedido> detalles = new ArrayList<>();

}
