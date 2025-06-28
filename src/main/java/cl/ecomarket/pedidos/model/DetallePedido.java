package cl.ecomarket.pedidos.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Table(name = "DETALLEPEDIDOS")
@Data
@Schema(description = "Detalle de cada producto incluido en un pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del detalle del pedido", example = "1")
    private Long detalleId;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    @Schema(description = "Pedido al que pertenece este detalle")
    private Pedido pedido;

    @Column(name = "producto_id", nullable = false)
    @Schema(description = "ID del producto", example = "2001")
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    @Schema(description = "Cantidad del producto", example = "3")
    private int cantidad;

    @Column(name = "precio_unitario")
    @Schema(description = "Precio unitario del producto", example = "15000.00")
    private BigDecimal precioUnitario;
}
