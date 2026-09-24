package com.javanauta.usuario.business.dto.response;

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
public class UsuarioResponse {

    private String nome;
    private String email;
    private String senha;
    private List<EnderecoResponse> enderecos;
    private List<TelefoneResponse> telefones;

}
