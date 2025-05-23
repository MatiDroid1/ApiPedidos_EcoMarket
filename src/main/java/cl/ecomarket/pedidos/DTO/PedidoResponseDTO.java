package cl.ecomarket.pedidos.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

// PedidoDTO.java
@Data
public class PedidoResponseDTO {
    private Long id;
    private Long clienteId;
    private Long tiendaId;
    private Long empleadoId;
    private String estado;
    private String metodoPago;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private LocalDateTime fechaPedido;
    private List<DetalleResponseDTO> detalles;

    @Data
    public static class DetalleResponseDTO {
        private Long productoId;
        private String nombre;
        private int cantidad;
        private BigDecimal precioUnitario;
    }
}


