package com.deliverytech.delivery_api.service;

import java.util.List;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.model.Pedido;

import java.time.LocalDateTime;

import java.util.Map;
import java.util.Optional;


public interface PedidoService {
    
    // Operações básicas
    Pedido realizar(PedidoRequest pedidoRequest);
    Optional<Pedido> buscarPorId(Long id);
    Optional<Pedido> buscarPorIdComItens(Long id);
    List<Pedido> listarPorCliente(Long clienteId);
    List<Pedido> listarPorRestaurante(Long restauranteId);
    List<Pedido> listarPorStatus(StatusPedido status);
    List<Pedido> listarPorRestauranteEStatus(Long restauranteId, StatusPedido status);
    
    // Gestão de status
    Pedido atualizarStatus(Long id, StatusPedido novoStatus);
    Pedido confirmar(Long id);
    Pedido iniciarPreparo(Long id);
    Pedido finalizarPreparo(Long id);
    Pedido iniciarEntrega(Long id);
    Pedido finalizarEntrega(Long id);
    Pedido cancelar(Long id);
    // Relatórios
    List<Pedido> listarPorPeriodo(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);
    Map<String, Object> obterEstatisticas(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);
    List<Produto> listarTodos();
}
