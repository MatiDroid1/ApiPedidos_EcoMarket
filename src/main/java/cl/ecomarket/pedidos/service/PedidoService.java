package cl.ecomarket.pedidos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.ecomarket.pedidos.DTO.PedidoResponseDTO;
import cl.ecomarket.pedidos.DTO.PedidoResponseDTO.DetalleResponseDTO;
import cl.ecomarket.pedidos.DTO.ProductoDTO;
import cl.ecomarket.pedidos.model.DetallePedido;
import cl.ecomarket.pedidos.model.Pedido;
import cl.ecomarket.pedidos.repository.PedidoRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setClienteId(pedidoActualizado.getClienteId());
            pedido.setTiendaId(pedidoActualizado.getTiendaId());
            pedido.setEmpleadoId(pedidoActualizado.getEmpleadoId());
            pedido.setFechaPedido(pedidoActualizado.getFechaPedido());
            pedido.setEstado(pedidoActualizado.getEstado());
            pedido.setSubtotal(pedidoActualizado.getSubtotal());
            pedido.setDescuento(pedidoActualizado.getDescuento());
            pedido.setTotal(pedidoActualizado.getTotal());
            pedido.setMetodoPago(pedidoActualizado.getMetodoPago());
            return pedidoRepository.save(pedido);
        }).orElse(null);
    }

    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Autowired
    private RestTemplate restTemplate;

    public ProductoDTO obtenerProductoPorId(Long productoId) {
        String url = "http://localhost:8081/api/v1/productos/" + productoId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "ecomarket1554"); // pon la API key correcta

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductoDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ProductoDTO.class);

        return response.getBody();
    }

  public void procesarPedido(PedidoResponseDTO pedidoDTO) {
    for (DetalleResponseDTO detalle : pedidoDTO.getDetalles()) {
        ProductoDTO producto = obtenerProductoPorId(detalle.getProductoId());

        System.out.println("Producto encontrado: " + producto.getNombre() + " - $" + producto.getPrecio());
        // Aquí calculas lo que necesites
    }
}

public PedidoResponseDTO mapToResponseDTO(Pedido pedido) {
    PedidoResponseDTO response = new PedidoResponseDTO();
    response.setId(pedido.getPedidoId());
    response.setClienteId(pedido.getClienteId());
    response.setTiendaId(pedido.getTiendaId());
    response.setEmpleadoId(pedido.getEmpleadoId());
    response.setEstado(pedido.getEstado());
    response.setMetodoPago(pedido.getMetodoPago());
    response.setSubtotal(pedido.getSubtotal());
    response.setDescuento(pedido.getDescuento());
    response.setTotal(pedido.getTotal());
    response.setFechaPedido(pedido.getFechaPedido());

    List<PedidoResponseDTO.DetalleResponseDTO> detallesResponse = new ArrayList<>();

    for (DetallePedido detalle : pedido.getDetalles()) {
        ProductoDTO producto = obtenerProductoPorId(detalle.getProductoId());

        PedidoResponseDTO.DetalleResponseDTO detalleResp = new PedidoResponseDTO.DetalleResponseDTO();
        detalleResp.setProductoId(detalle.getProductoId());
        detalleResp.setCantidad(detalle.getCantidad());
        detalleResp.setPrecioUnitario(detalle.getPrecioUnitario());

        if (producto != null) {
            detalleResp.setNombre(producto.getNombre());
        } else {
            detalleResp.setNombre("Producto no encontrado");
        }

        detallesResponse.add(detalleResp);
    }

    response.setDetalles(detallesResponse);

    return response;
}

public List<PedidoResponseDTO> obtenerTodosConNombres() {
    List<Pedido> pedidos = pedidoRepository.findAll();
    List<PedidoResponseDTO> pedidosResponse = new ArrayList<>();

    for (Pedido pedido : pedidos) {
        PedidoResponseDTO dto = mapToResponseDTO(pedido); // usa el método que armamos antes
        pedidosResponse.add(dto);
    }

    return pedidosResponse;
}


}
