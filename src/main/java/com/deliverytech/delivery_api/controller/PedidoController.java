package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // corrigido
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Realizar um novo pedido
     * POST /pedidos
     */
    @PostMapping
    public ResponseEntity<?> realizar(@Valid @RequestBody PedidoRequest pedidoRequest) {
        try {
            log.info("Recebida requisição para realizar pedido");
            Pedido pedidoSalvo = pedidoService.realizar(pedidoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao realizar pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao realizar pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Buscar pedido por ID
     * GET /pedidos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar pedido por ID: {}", id);
        return pedidoService.buscarPorIdComItens(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Listar pedidos por cliente
     * GET /pedidos/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> listarPorCliente(@PathVariable Long clienteId) {
        log.info("Recebida requisição para listar pedidos do cliente ID: {}", clienteId);
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Listar pedidos por restaurante
     * GET /pedidos/restaurante/{restauranteId}
     */
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Pedido>> listarPorRestaurante(@PathVariable Long restauranteId) {
        log.info("Recebida requisição para listar pedidos do restaurante ID: {}", restauranteId);
        List<Pedido> pedidos = pedidoService.listarPorRestaurante(restauranteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Listar pedidos por status
     * GET /pedidos/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> listarPorStatus(@PathVariable StatusPedido status) {
        log.info("Recebida requisição para listar pedidos com status: {}", status);
        List<Pedido> pedidos = pedidoService.listarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Listar pedidos por restaurante e status
     * GET /pedidos/restaurante/{restauranteId}/status/{status}
     */
    @GetMapping("/restaurante/{restauranteId}/status/{status}")
    public ResponseEntity<List<Pedido>> listarPorRestauranteEStatus(
            @PathVariable Long restauranteId,
            @PathVariable StatusPedido status) {
        log.info("Recebida requisição para listar pedidos do restaurante ID: {} com status: {}", restauranteId, status);
        List<Pedido> pedidos = pedidoService.listarPorRestauranteEStatus(restauranteId, status);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Atualizar status do pedido
     * PATCH /pedidos/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        try {
            log.info("Recebida requisição para atualizar status do pedido ID: {} para {}", id, status);
            Pedido pedido = pedidoService.atualizarStatus(id, status);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar status do pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao atualizar status do pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Confirmar pedido
     * PATCH /pedidos/{id}/confirmar
     */
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para confirmar pedido ID: {}", id);
            Pedido pedido = pedidoService.confirmar(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao confirmar pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao confirmar pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Iniciar preparo do pedido
     * PATCH /pedidos/{id}/iniciar-preparo
     */
    @PatchMapping("/{id}/iniciar-preparo")
    public ResponseEntity<?> iniciarPreparo(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para iniciar preparo do pedido ID: {}", id);
            Pedido pedido = pedidoService.iniciarPreparo(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao iniciar preparo do pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao iniciar preparo do pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Finalizar preparo do pedido
     * PATCH /pedidos/{id}/finalizar-preparo
     */
    @PatchMapping("/{id}/finalizar-preparo")
    public ResponseEntity<?> finalizarPreparo(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para finalizar preparo do pedido ID: {}", id);
            Pedido pedido = pedidoService.finalizarPreparo(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao finalizar preparo do pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao finalizar preparo do pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Iniciar entrega do pedido
     * PATCH /pedidos/{id}/iniciar-entrega
     */
    @PatchMapping("/{id}/iniciar-entrega")
    public ResponseEntity<?> iniciarEntrega(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para iniciar entrega do pedido ID: {}", id);
            Pedido pedido = pedidoService.iniciarEntrega(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao iniciar entrega do pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao iniciar entrega do pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Finalizar entrega do pedido
     * PATCH /pedidos/{id}/finalizar-entrega
     */
    @PatchMapping("/{id}/finalizar-entrega")
    public ResponseEntity<?> finalizarEntrega(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para finalizar entrega do pedido ID: {}", id);
            Pedido pedido = pedidoService.finalizarEntrega(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao finalizar entrega do pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao finalizar entrega do pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Cancelar pedido
     * PATCH /pedidos/{id}/cancelar
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para cancelar pedido ID: {}", id);
            Pedido pedido = pedidoService.cancelar(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao cancelar pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao cancelar pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Listar pedidos por período
     * GET /pedidos/periodo?restauranteId={id}&inicio={data}&fim={data}
     */
    @GetMapping("/periodo")
    public ResponseEntity<?> listarPorPeriodo(
            @RequestParam Long restauranteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        try {
            log.info("Recebida requisição para listar pedidos do restaurante ID: {} no período de {} a {}", 
                restauranteId, inicio, fim);
                
            LocalDateTime dataInicio = inicio.atStartOfDay();
            LocalDateTime dataFim = fim.atTime(LocalTime.MAX);
            
            List<Pedido> pedidos = pedidoService.listarPorPeriodo(restauranteId, dataInicio, dataFim);
            return ResponseEntity.ok(pedidos);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao listar pedidos por período: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao listar pedidos por período", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Obter estatísticas de pedidos por período
     * GET /pedidos/estatisticas?restauranteId={id}&inicio={data}&fim={data}
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<?> obterEstatisticas(
            @RequestParam Long restauranteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        try {
            log.info("Recebida requisição para obter estatísticas do restaurante ID: {} no período de {} a {}", 
                restauranteId, inicio, fim);
                
            LocalDateTime dataInicio = inicio.atStartOfDay();
            LocalDateTime dataFim = fim.atTime(LocalTime.MAX);
            
            Map<String, Object> estatisticas = pedidoService.obterEstatisticas(restauranteId, dataInicio, dataFim);
            return ResponseEntity.ok(estatisticas);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao obter estatísticas: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao obter estatísticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }
}
