package com.deliverytech.delivery_api.model;

public enum StatusPedido {
    REALIZADO,      // Pedido foi recebido pelo sistema
    CONFIRMADO,     // Restaurante confirmou o recebimento do pedido
    EM_PREPARO,     // Restaurante está preparando o pedido
    PRONTO,         // Pedido está pronto para entrega/retirada
    EM_ENTREGA,     // Pedido está a caminho do cliente
    ENTREGUE,       // Pedido foi entregue ao cliente
    CANCELADO       // Pedido foi cancelado
}
