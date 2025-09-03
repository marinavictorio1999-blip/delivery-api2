package com.deliverytech.delivery_api.controller;



import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {


    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;


    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest request) {
        Restaurante restaurante = restauranteService.buscarPorId(request.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));


        Produto produto = Produto.builder()
                .nome(request.getNome())
                .categoria(request.getCategoria())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .ativo(true)
                .restaurante(restaurante)
                .build();


        Produto salvo = produtoService.cadastrar(request);//corrido
        return ResponseEntity.status(201).body(new ProdutoResponse(
                salvo.getId(), salvo.getNome(), salvo.getCategoria(), 
                salvo.getDescricao(), salvo.getPreco(), salvo.getAtivo()));
    }


    @GetMapping("/restaurante/{restauranteId}")
    public List<ProdutoResponse> listarPorRestaurante(@PathVariable Long restauranteId) {
        return produtoService.listarPorRestaurante(restauranteId).stream()
                .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(),
                    p.getDescricao(), p.getPreco(), p.getAtivo()))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        Produto atualizado = Produto.builder()
                .nome(request.getNome())
                .categoria(request.getCategoria())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .build();
        Produto salvo = produtoService.atualizar(id, request); // corrigido
        return ResponseEntity.ok(new ProdutoResponse(salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getDescricao(), salvo.getPreco(), salvo.getAtivo()));
    }


    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id) {
        produtoService.alterarDisponibilidade(id);
        return ResponseEntity.noContent().build();
    }


    // ADICIONAR: Listar todos os produtos
    @GetMapping
    public List<ProdutoResponse> listarTodos() {
        return produtoService.listarTodos().stream()
                .map(p -> new ProdutoResponse(
                    p.getId(), p.getNome(), p.getCategoria(),
                    p.getDescricao(), p.getPreco(), p.getAtivo()))
                .collect(Collectors.toList());
    }


    // ADICIONAR: Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(),
                     p.getDescricao(), p.getPreco(), p.getAtivo()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Buscar produtos por categoria
     * GET /api/produtos/categoria/{categoria}
     */
   @GetMapping("/categoria/{categoria}") // corrigido 
    public List<ProdutoResponse> buscarPorCategoria(
            @RequestParam Long restauranteId, 
            @PathVariable String categoria) {
        return produtoService.buscarPorCategoria(restauranteId, categoria).stream()
                .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(),
                    p.getDescricao(), p.getPreco(), p.getAtivo()))
                .collect(Collectors.toList());
    }

    /**
     * Busca produtos por nome
     * GET /api/produtos/buscar?nome={nome}
     */
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(
            @RequestParam Long restauranteId, 
            @RequestParam String nome) {
        List<Produto> produtos = produtoService.buscarPorNome(restauranteId, nome);
        List<ProdutoResponse> response = produtos.stream()
            .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(),
                p.getDescricao(), p.getPreco(), p.getAtivo()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
