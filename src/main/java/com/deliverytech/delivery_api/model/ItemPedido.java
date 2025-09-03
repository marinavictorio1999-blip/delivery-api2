package com.deliverytech.delivery_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    
    private Integer quantidade;
    
    private BigDecimal precoUnitario;
    
    private BigDecimal subtotal;
    
    private String observacao;
    
    // Método para calcular o subtotal
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
    
    // Método para inicializar o preço unitário a partir do produto
    public void inicializarPrecoUnitario() {
        if (produto != null && produto.getPreco() != null) {
            this.precoUnitario = produto.getPreco();
        }
    }
}
