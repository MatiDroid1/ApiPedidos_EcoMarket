package cl.ecomarket.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ecomarket.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}