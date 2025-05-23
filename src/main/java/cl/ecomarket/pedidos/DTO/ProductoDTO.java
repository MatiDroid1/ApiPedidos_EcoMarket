package cl.ecomarket.pedidos.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long productoId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
}