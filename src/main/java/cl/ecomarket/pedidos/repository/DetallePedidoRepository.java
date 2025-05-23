package cl.ecomarket.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ecomarket.pedidos.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    // Puedes agregar métodos personalizados si los necesitas
}