package com.javanauta.usuario.business.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequest {

    private String nome;
    private String email;
    private String senha;
    private List<EnderecoRequest> enderecos;
    private List<TelefoneRequest> telefones;

}
