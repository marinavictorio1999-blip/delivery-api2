package com.deliverytech.delivery_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

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
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private boolean disponivel; 
    


    
    @Column(length = 1000)
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    private String categoria;
    
    @Column(length = 255)
    private String imagemUrl;
    
    @Builder.Default
    private Boolean ativo = true;
    
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    // Relacionamento com Restaurante (muitos-para-um)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnore
    private Restaurante restaurante;
    
    // Relacionamento com ItemPedido (um-para-muitos)
    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<ItemPedido> itensPedido = new ArrayList<>();
    
    // Métodos auxiliares
    @JsonIgnore
    public void indisponibilizar() {
        this.ativo = false;
    }
    
    @JsonIgnore
    public void disponibilizar() {
        this.ativo = true;
    }
}
