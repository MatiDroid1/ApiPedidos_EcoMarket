package cl.ecomarket.pedidos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import cl.ecomarket.pedidos.DTO.ProductoDTO;
import cl.ecomarket.pedidos.model.DetallePedido;
import cl.ecomarket.pedidos.model.Pedido;
import cl.ecomarket.pedidos.repository.PedidoRepository;
import cl.ecomarket.pedidos.service.PedidoService;
import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PedidoServiceIntegrationTest {

    @Autowired
    private PedidoService pedidoService;
 
    @Test
    public void testGuardarPedidoConDetalle() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(26L);
        pedido.setTiendaId(1L);
        pedido.setEmpleadoId(25L);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("pendiente");
        pedido.setSubtotal(new BigDecimal("10000"));
        pedido.setDescuento(BigDecimal.ZERO);
        pedido.setTotal(new BigDecimal("10000"));
        pedido.setMetodoPago("EFECTIVO");

        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(3L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(new BigDecimal("5000"));

        // Asociación bidireccional
        pedido.getDetalles().add(detalle);
        detalle.setPedido(pedido);

        Pedido guardado = pedidoService.guardarPedido(pedido);

        assertNotNull(guardado.getPedidoId());
        assertEquals(1, guardado.getDetalles().size());
        assertEquals("pendiente", guardado.getEstado());

        System.out.println("✅ Pedido guardado con ID: " + guardado.getPedidoId());
    }

@Test
public void testBuscarPedidoPorId() {
    Optional<Pedido> encontrado = pedidoService.obtenerPorId(125L);
    assertTrue(encontrado.isPresent());

    Pedido p = encontrado.get();
    System.out.println("Pedido encontrado: ID=" + p.getPedidoId() + ", Estado=" + p.getEstado());

    assertEquals("pendiente", p.getEstado(), "ClienteId esperado: " + p.getClienteId());
    assertNotNull(p.getDetalles());
    System.out.println("Cantidad de detalles: " + p.getDetalles().size());
    p.getDetalles().forEach(d -> 
        System.out.println("Detalle: productoId=" + d.getProductoId() + ", cantidad=" + d.getCantidad())
    );

    assertEquals(1, p.getDetalles().size(), "ClienteId esperado: " + p.getClienteId());
}

@Test
public void testActualizarEstadoPedido() {
    Optional<Pedido> optPedido = pedidoService.obtenerPorId(125L);
    assertTrue(optPedido.isPresent());

    Pedido pedido = optPedido.get();
    pedido.setEstado("enviado");

    Pedido actualizado = pedidoService.guardarPedido(pedido);
    assertEquals("enviado", actualizado.getEstado());

    System.out.println("Estado actualizado a: " + actualizado.getEstado());
}



}