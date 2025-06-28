package cl.ecomarket.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.ecomarket.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
     @Modifying
    @Query("UPDATE Pedido p SET p.estado = 'cancelado' WHERE p.pedidoId = :id AND p.estado NOT IN ('entregado', 'cancelado')")
    int cancelarPedido(@Param("id") Long id);
}