package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    List<ItemPedido> findByPedidoId(Long pedidoId);
    
    @Query("SELECT SUM(i.quantidade) FROM ItemPedido i WHERE i.produto.id = :produtoId")
    Integer countQuantidadeByProdutoId(@Param("produtoId") Long produtoId);
    
    @Query("SELECT i.produto.id, i.produto.nome, SUM(i.quantidade) as total FROM ItemPedido i " +
           "WHERE i.pedido.restaurante.id = :restauranteId " +
           "GROUP BY i.produto.id, i.produto.nome ORDER BY total DESC")
    List<Object[]> findMostSoldProductsByRestaurante(@Param("restauranteId") Long restauranteId);
}
