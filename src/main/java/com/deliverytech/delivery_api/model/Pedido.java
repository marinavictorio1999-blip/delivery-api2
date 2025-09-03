package com.deliverytech.delivery_api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    private BigDecimal total;
    
    private String enderecoEntrega;
    
    private String observacao;
    
    private LocalDateTime dataPedido;
    
    private LocalDateTime dataEntrega;
    
    // Relacionamento com ItemPedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    // Método para adicionar item ao pedido
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
        recalcularTotal();
    }
    
    // Método para remover item do pedido
    public void removerItem(ItemPedido item) {
        itens.remove(item);
        recalcularTotal();
    }
    
    // Método para recalcular o total do pedido
    public void recalcularTotal() {
        this.total = itens.stream()
            .map(ItemPedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Método para verificar se o pedido pode ser atualizado
    public boolean podeSerAtualizado() {
        return this.status == StatusPedido.REALIZADO || 
               this.status == StatusPedido.CONFIRMADO;
    }
    
    // Método para verificar se o pedido pode ser cancelado
    public boolean podeSerCancelado() {
        return this.status != StatusPedido.EM_ENTREGA && 
               this.status != StatusPedido.ENTREGUE && 
               this.status != StatusPedido.CANCELADO;
    }
    
    // Método para atualizar status
    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
        
        if (novoStatus == StatusPedido.ENTREGUE) {
            this.dataEntrega = LocalDateTime.now();
        }
    }
}
