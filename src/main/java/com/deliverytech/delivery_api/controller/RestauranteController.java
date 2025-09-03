package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.ApiResponseWrapper;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    /**
     * Cadastrar novo restaurante
     * POST /restaurantes
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody RestauranteRequest restauranteRequest) {
        try {
            log.info("Recebida requisição para cadastrar restaurante: {}", restauranteRequest.getNome());
            Restaurante restauranteSalvo = restauranteService.cadastrar(restauranteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteSalvo);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao cadastrar restaurante: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao cadastrar restaurante", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Listar todos os restaurantes ativos
     * GET /restaurantes
     */
    @GetMapping
    public ResponseEntity<List<Restaurante>> listar() {
        log.info("Recebida requisição para listar restaurantes ativos");
        List<Restaurante> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Buscar restaurante por ID
     * GET /restaurantes/{id}
     */
    @GetMapping("/{id}") // corrigido
    public ResponseEntity<ApiResponseWrapper<Restaurante>> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar restaurante por ID: {}", id);

        return restauranteService.buscarPorId(id)
            .map(restaurante -> ResponseEntity.ok(new ApiResponseWrapper<>(true, restaurante, "Restaurante encontrado")))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseWrapper<>(false, null, "Restaurante não encontrado")));
    }


    /**
     * Buscar restaurante por CNPJ
     * GET /restaurantes/cnpj/{cnpj}
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<?> buscarPorCnpj(@PathVariable String cnpj) {
        log.info("Recebida requisição para buscar restaurante por CNPJ: {}", cnpj);
        return restauranteService.buscarPorCnpj(cnpj)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar restaurantes por nome
     * GET /restaurantes/buscar?nome=Pizzaria
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Restaurante>> buscarPorNome(@RequestParam String nome) {
        log.info("Recebida requisição para buscar restaurantes por nome: {}", nome);
        List<Restaurante> restaurantes = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Buscar restaurantes por especialidade
     * GET /restaurantes/especialidade/{especialidade}
     */
    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<Restaurante>> buscarPorEspecialidade(@PathVariable String especialidade) {
        log.info("Recebida requisição para buscar restaurantes por especialidade: {}", especialidade);
        List<Restaurante> restaurantes = restauranteService.buscarPorEspecialidade(especialidade);
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Atualizar restaurante
     * PUT /api/restaurantes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                      @Valid @RequestBody RestauranteRequest restauranteRequest) {
        try {
            log.info("Recebida requisição para atualizar restaurante ID: {}", id);
            Restaurante restauranteAtualizado = restauranteService.atualizar(id, restauranteRequest);
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar restaurante: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao atualizar restaurante", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Inativar restaurante (soft delete)
     * DELETE /restaurantes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para inativar restaurante ID: {}", id);
            restauranteService.inativar(id);
            return ResponseEntity.ok().body("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao inativar restaurante: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao inativar restaurante", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Ativar/Desativar restaurante (toggle status ativo)
     * PATCH /api/restaurantes/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id) {
        try {
            log.info("Recebida requisição para alternar status do restaurante ID: {}", id);
            Restaurante restaurante = restauranteService.ativarDesativarRestaurante(id);
            String status = restaurante.getAtivo() ? "ativado" : "desativado";
            return ResponseEntity.ok().body("Restaurante " + status + " com sucesso");
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao alternar status do restaurante: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao alternar status do restaurante", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
        }
    }

    /**
     * Buscar restaurantes com produtos disponíveis
     * GET /restaurantes/com-produtos-disponiveis
     */
    @GetMapping("/com-produtos-disponiveis")
    public ResponseEntity<List<Restaurante>> buscarComProdutosDisponiveis() {
        log.info("Recebida requisição para buscar restaurantes com produtos disponíveis");
        List<Restaurante> restaurantes = restauranteService.buscarComProdutosDisponiveis();
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Ranking de restaurantes por número de pedidos
     * GET /restaurantes/ranking
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<Map<String, Object>>> rankingPorPedidos() {
        log.info("Recebida requisição para ranking de restaurantes por pedidos");
        List<Object[]> ranking = restauranteService.rankingPorPedidos();
        
        List<Map<String, Object>> response = ranking.stream()
            .map(row -> {
                Map<String, Object> item = new HashMap<>();
                item.put("nome", row[0]);
                item.put("totalPedidos", row[1]);
                return item;
            })
            .toList();
        
        return ResponseEntity.ok(response);
    }
}
