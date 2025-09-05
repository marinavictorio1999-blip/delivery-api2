package com.deliverytech.delivery_api.config;
 
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
 
import java.util.Arrays;
import java.util.List;
 
 
@Component
public class DataLoader implements CommandLineRunner {
 
    @Autowired
    private ClienteRepository clienteRepository;
 
    @Autowired
    private RestauranteRepository restauranteRepository;
 
    @Autowired
    private ProdutoRepository produtoRepository;
 
    @Autowired
    private PedidoRepository pedidoRepository;
 
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");
 
     
        // Inserir dados de teste (sem limpar dados existentes)
        inserirClientes();
        inserirRestaurantes();
 
        System.out.println("=== CARGA DE DADOS CONCLUÍDA ===");
       
        // ✅ ADICIONAR: Spring Boot iniciada com sucesso + Bender
        System.out.println("\n✅ Spring Boot Application iniciada com sucesso!");
       
       
        // ✅ INFORMAR sobre captura automática
        System.out.println("\n🎯 SISTEMA DE CAPTURA AUTOMÁTICA ATIVO!");
        System.out.println("📁 Respostas serão salvas em: ./entregaveis/");
        System.out.println("🔄 Faça requisições para /api/* e veja os arquivos sendo gerados!\n");
    }
 
 
private void inserirClientes() {
    System.out.println("--- Inserindo clientes ---");
 
    Cliente cliente1 = new Cliente(null, "João Silva", "joao@email.com",
            "11987654321", "Rua das Flores, 123 - Vila Madalena, São Paulo - SP",
            null, true, null);
 
    Cliente cliente2 = new Cliente(null, "Maria Santos", "maria@email.com",
            "11876543210", "Av. Paulista, 456 - Bela Vista, São Paulo - SP",
            null, true, null);
 
    Cliente cliente3 = new Cliente(null, "Pedro Oliveira", "pedro@email.com",
            "11765432109", "Rua Augusta, 789 - Consolação, São Paulo - SP",
            null, false, null);
 
    Cliente cliente4 = new Cliente(null, "Ana Costa", "ana@email.com",
            "11654321098", "Rua Oscar Freire, 321 - Jardins, São Paulo - SP",
            null, true, null);
 
    Cliente cliente5 = new Cliente(null, "Carlos Ferreira", "carlos@email.com",
            "11543210987", "Rua 25 de Março, 654 - Centro, São Paulo - SP",
            null, true, null);
 
    List<Cliente> clientes = Arrays.asList(cliente1, cliente2, cliente3, cliente4, cliente5);
 
    for (Cliente cliente : clientes) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isEmpty()) {
            clienteRepository.save(cliente);
            System.out.println("✓ Cliente " + cliente.getNome() + " inserido");
        } else {
            System.out.println("⚠ Cliente " + cliente.getNome() + " já existe, ignorando...");
        }
    }
}
 
private void inserirRestaurantes() {
    System.out.println("--- Inserindo Restaurantes ---");
 
    Restaurante restaurante1 = Restaurante.builder()
            .nome("Pizza Express")
            .categoria("Italiana")
            .telefone("1133333333")
            .TaxaEntrega(new BigDecimal("3.50"))
            .ativo(true)
            .build();
 
    Restaurante restaurante2 = Restaurante.builder()
            .nome("Burger King")
            .categoria("Fast Food")
            .telefone("1144444444")
            .TaxaEntrega(new BigDecimal("5.00"))
            .ativo(true)
            .build();
 
    List<Restaurante> restaurantes = Arrays.asList(restaurante1, restaurante2);
 
    // Salvar restaurantes se não existirem
    for (Restaurante restaurante : restaurantes) {
        if (restauranteRepository.findByNome(restaurante.getNome()).isEmpty()) {
            restauranteRepository.save(restaurante);
            System.out.println("✓ Restaurante " + restaurante.getNome() + " inserido");
        } else {
            System.out.println("⚠ Restaurante " + restaurante.getNome() + " já existe, ignorando...");
        }
    }
 
    // Agora sim, chamar inserirProdutos()
   
}
 
 
 
 
private void inserirProdutos() {
    System.out.println("--- Inserindo Produtos ---");
 
    var restaurantes = restauranteRepository.findAll();
 
    var pizzaExpress = restaurantes.stream()
            .filter(r -> r.getNome().equals("Pizza Express"))
            .findFirst().orElse(null);
 
    var burgerKing = restaurantes.stream()
            .filter(r -> r.getNome().equals("Burger King"))
            .findFirst().orElse(null);
 
    var sushiHouse = restaurantes.stream()
            .filter(r -> r.getNome().equals("Sushi House"))
            .findFirst().orElse(null);
 
    var gyrosAthenas = restaurantes.stream()
            .filter(r -> r.getNome().equals("Gyros Athenas"))
            .findFirst().orElse(null);
 
    var chipariaPorto = restaurantes.stream()
            .filter(r -> r.getNome().equals("Chiparia do Porto"))
            .findFirst().orElse(null);
 
    // Criando produtos
    List<Produto> produtos = Arrays.asList(
        Produto.builder()
            .nome("Pizza Margherita")
            .categoria("Pizza")
            .descricao("Pizza clássica com molho de tomate, mussarela e manjericão")
            .preco(new BigDecimal("25.90"))
            .restaurante(pizzaExpress)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Pizza Pepperoni")
            .categoria("Pizza")
            .descricao("Pizza com molho de tomate, mussarela e pepperoni")
            .preco(new BigDecimal("29.90"))
            .restaurante(pizzaExpress)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Big Burger")
            .categoria("Hambúrguer")
            .descricao("Hambúrguer duplo com queijo, alface, tomate e molho especial")
            .preco(new BigDecimal("18.50"))
            .restaurante(burgerKing)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Batata Frita Grande")
            .categoria("Acompanhamento")
            .descricao("Porção grande de batatas fritas crocantes")
            .preco(new BigDecimal("8.90"))
            .restaurante(burgerKing)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Sushi Salmão")
            .categoria("Sushi")
            .descricao("8 peças de sushi de salmão fresco")
            .preco(new BigDecimal("32.00"))
            .restaurante(sushiHouse)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Hot Roll")
            .categoria("Sushi")
            .descricao("8 peças de hot roll empanado com salmão")
            .preco(new BigDecimal("28.50"))
            .restaurante(sushiHouse)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Gyros de Cordeiro")
            .categoria("Espeto")
            .descricao("Espeto de cordeiro grelhado com molho tzatziki, tomate e cebola roxa")
            .preco(new BigDecimal("35.90"))
            .restaurante(gyrosAthenas)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Souvlaki de Frango")
            .categoria("Espeto")
            .descricao("Espetinho de frango marinado com ervas gregas e batata frita")
            .preco(new BigDecimal("28.50"))
            .restaurante(gyrosAthenas)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Fish & Chips Tradicional")
            .categoria("Peixe")
            .descricao("Filé de bacalhau empanado com batatas fritas e molho tártaro")
            .preco(new BigDecimal("42.90"))
            .restaurante(chipariaPorto)
            .ativo(true)
            .build(),
 
        Produto.builder()
            .nome("Porção de Camarão Empanado")
            .categoria("Frutos do Mar")
            .descricao("500g de camarão empanado com molho agridoce")
            .preco(new BigDecimal("52.00"))
            .restaurante(chipariaPorto)
            .ativo(true)
            .build()
    );
 
    // Salvar todos os produtos
   for (Produto produto : produtos) {
    // Verifica se já existe produto com mesmo nome no mesmo restaurante
    var existente = produtoRepository.findByNomeAndRestaurante_Id(produto.getNome(), produto.getRestaurante().getId());
 
    if (existente.isEmpty()) {
        produtoRepository.save(produto);
        System.out.println("✓ Produto " + produto.getNome() + " inserido");
    } else {
        System.out.println("⚠ Produto " + produto.getNome() + " já existe no restaurante "
                           + produto.getRestaurante().getNome() + ", ignorando...");
    }
}
 
}
 
 
 
}