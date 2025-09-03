package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByRestauranteId(Long restauranteId);
    List<Pedido> findByStatus(StatusPedido status);
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    // Query com JOIN FETCH para carregar itens
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto WHERE p.id = :id")
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);

    // Query com JOIN FETCH para carregar itens por cliente
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto WHERE p.cliente.id = :clienteId")
    List<Pedido> findByClienteIdWithItens(@Param("clienteId") Long clienteId);
    
    // Buscar pedidos por restaurante e status
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.status = :status ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteIdAndStatus(
        @Param("restauranteId") Long restauranteId,
        @Param("status") StatusPedido status);

    // Buscar pedidos por período e restaurante
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId " +
           "AND p.dataPedido BETWEEN :inicio AND :fim ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteIdAndPeriodo(
        @Param("restauranteId") Long restauranteId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim);
        
    // Buscar totais por período para um restaurante
    @Query("SELECT COUNT(p), SUM(p.total) FROM Pedido p WHERE p.restaurante.id = :restauranteId " +
           "AND p.status = 'ENTREGUE' AND p.dataPedido BETWEEN :inicio AND :fim")
    Object[] getTotaisPorPeriodo(
        @Param("restauranteId") Long restauranteId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim);
}
