package com.deliverytech.delivery_api.dto.request;

import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "ID do restaurante é obrigatório")
    private Long restauranteId;
    
    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    @Valid
    private List<ItemPedidoRequest> itens;
    
    @Size(max = 200, message = "Observação não pode ter mais de 200 caracteres")
    private String observacao;
    
    @NotNull(message = "Endereço de entrega é obrigatório")
    @Size(min = 5, max = 200, message = "Endereço deve ter entre 5 e 200 caracteres")
    private String enderecoEntrega;
}
