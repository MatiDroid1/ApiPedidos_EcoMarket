package cl.ecomarket.pedidos.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.ecomarket.pedidos.DTO.PedidoResponseDTO;
import cl.ecomarket.pedidos.DTO.PedidoResponseDTO.DetalleResponseDTO;
import cl.ecomarket.pedidos.DTO.ProductoDTO;
import cl.ecomarket.pedidos.model.DetallePedido;
import cl.ecomarket.pedidos.model.Pedido;
import cl.ecomarket.pedidos.service.PedidoService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin
@Tag(name = "Pedidos", description = "API para gestionar pedidos de clientes")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido", description = "Crea un pedido con sus detalles y calcula totales")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoResponseDTO pedidoDTO) {
        try {
            Pedido pedido = new Pedido();
            pedido.setClienteId(pedidoDTO.getClienteId());
            pedido.setTiendaId(pedidoDTO.getTiendaId());
            pedido.setEmpleadoId(pedidoDTO.getEmpleadoId());
            pedido.setEstado(pedidoDTO.getEstado());
            pedido.setMetodoPago(pedidoDTO.getMetodoPago());
            pedido.setFechaPedido(LocalDateTime.now());

            AtomicReference<BigDecimal> subtotal = new AtomicReference<>(BigDecimal.ZERO);

            List<DetallePedido> detalles = new ArrayList<>();

            for (PedidoResponseDTO.DetalleResponseDTO detalleDTO : pedidoDTO.getDetalles()) {
                ProductoDTO producto = pedidoService.obtenerProductoPorId(detalleDTO.getProductoId());

                if (producto == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Producto con id " + detalleDTO.getProductoId() + " no encontrado.");
                }

                DetallePedido detalle = new DetallePedido();
                detalle.setProductoId(detalleDTO.getProductoId());
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setPedido(pedido);

                BigDecimal totalPorProducto = producto.getPrecio()
                        .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
                subtotal.updateAndGet(v -> v.add(totalPorProducto));

                detalles.add(detalle);
            }

            pedido.setSubtotal(subtotal.get());
            pedido.setDescuento(pedidoDTO.getDescuento() != null ? pedidoDTO.getDescuento() : BigDecimal.ZERO);
            pedido.setTotal(subtotal.get().subtract(pedido.getDescuento()));
            pedido.setDetalles(detalles);

            Pedido pedidoGuardado = pedidoService.guardarPedido(pedido);
            return ResponseEntity.ok(pedidoGuardado);

        } catch (Exception ex) {
            ex.printStackTrace(); // Para debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error interno al procesar la solicitud. " + ex.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos", description = "Devuelve todos los pedidos realizados")
 public List<PedidoResponseDTO> obtenerTodos() {
    return pedidoService.obtenerTodosConNombres();
}

@GetMapping("/{id}/detalladahetoas")
@Operation(summary = "Listar todos los pedidos", description = "Devuelve todos los pedidos realizados con url de los otros microservicios")
public ResponseEntity<?> getResenaDetalladahtoas(@PathVariable Long id) {
    ProductoDTO dto = pedidoService.obtenerProductoPorId(id);
    if (dto == null)
        return ResponseEntity.notFound().build();

    EntityModel<ProductoDTO> model = EntityModel.of(dto);
    model.add(linkTo(methodOn(PedidoController.class).getResenaDetalladahtoas(id)).withSelfRel());
    model.add(linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("todas-las-resenas"));
    model.add(Link.of("http://localhost:8081/api/v1/productos/" + dto.getProductoId())
            .withRel("producto"));

    return ResponseEntity.ok(model);
}


    @GetMapping("/{id}")
    @Operation(summary = "Listar todos los pedidos", description = "Devuelve todos pedido realizados buscando por su id")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(id);
        if (pedidoOpt.isPresent()) {
            PedidoResponseDTO responseDTO = pedidoService.mapToResponseDTO(pedidoOpt.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Listar todos los pedidos", description = "actualizar pedido")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            Pedido pedidoActualizado = pedidoService.actualizarPedido(id, pedido);
            if (pedidoActualizado == null) {
                // Si no encontró el pedido para actualizar
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            // Manejo general de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Listar todos los pedidos", description = "Eliminar pedido por id")
    public void eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
    }
}