package cl.ecomarket.pedidos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import cl.ecomarket.pedidos.DTO.ProductoDTO;
import cl.ecomarket.pedidos.repository.PedidoRepository;
import cl.ecomarket.pedidos.service.PedidoService;

public class PedidoServiceTest {

    @Mock
    private PedidoRepository pRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidoService pService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerProductoPorId() {
        Long productoId = 1L;

        ProductoDTO mockProducto = new ProductoDTO();
        mockProducto.setProductoId(productoId);
        mockProducto.setNombre("Producto Fake");
        mockProducto.setPrecio(new BigDecimal("1500"));

        // Simulo la respuesta que da RestTemplate
        ResponseEntity<ProductoDTO> mockResponse = ResponseEntity.ok(mockProducto);

        when(restTemplate.exchange(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
                org.mockito.ArgumentMatchers.any(HttpEntity.class),
                org.mockito.ArgumentMatchers.eq(ProductoDTO.class)))
            .thenReturn(mockResponse);

        ProductoDTO producto = pService.obtenerProductoPorId(productoId);

        assertNotNull(producto);
        assertEquals("Producto Fake", producto.getNombre());
        assertEquals(new BigDecimal("1500"), producto.getPrecio());

        verify(restTemplate).exchange(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.eq(HttpMethod.GET),
            org.mockito.ArgumentMatchers.any(HttpEntity.class),
            org.mockito.ArgumentMatchers.eq(ProductoDTO.class));
    }
}