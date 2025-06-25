package cl.ecomarket.pedidos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cl.ecomarket.pedidos.model.Pedido;
import cl.ecomarket.pedidos.repository.PedidoRepository;
import cl.ecomarket.pedidos.service.PedidoService;

public class PedidosServiceTest {
     @Mock
    private PedidoRepository pRepository;

    @InjectMocks
    private PedidoService pService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarPorId() {
        Pedido pedido = new Pedido();
        pedido.setPedidoId(5L);
        pedido.setClienteId(26L);
        pedido.setEstado("pendiente");
        pedido.setFechaPedido(LocalDateTime.now());

        when(pRepository.findById(5L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> resultado = pService.obtenerPorId(5L);
        assertNotNull(resultado);
        assertEquals(26L, resultado.get().getClienteId());
    }

    @Test
    public void testGuardarPedido() {
        Pedido pedido = new Pedido();
        pedido.setClienteId(26L);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("enviado");

        when(pRepository.save(pedido)).thenReturn(pedido);

        Pedido resultado = pService.guardarPedido(pedido);
        assertNotNull(resultado);
        assertEquals("enviado", resultado.getEstado());
    }

    @Test
    public void testListarTodos() {
        Pedido p1 = new Pedido();
        p1.setPedidoId(5L);
        Pedido p2 = new Pedido();
        p2.setPedidoId(10L);

        List<Pedido> lista = Arrays.asList(p1, p2);

        when(pRepository.findAll()).thenReturn(lista);

        List<Pedido> resultado = pService.obtenerTodos();
        assertEquals(2, resultado.size());
    }

}
