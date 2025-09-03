package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import lombok.Data;

@Data
public class UserResponse {

    private String nome;
    private String email;
    private Role role;

    public static UserResponse fromEntity(Usuario usuario) {
        UserResponse dto = new UserResponse();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole()); // agora Role é reconhecido
        return dto;
    }
}




