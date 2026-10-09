package com.javanauta.usuario.business.converter;

import com.javanauta.usuario.business.dto.request.EnderecoRequest;
import com.javanauta.usuario.business.dto.request.TelefoneRequest;
import com.javanauta.usuario.business.dto.request.UsuarioRequest;
import com.javanauta.usuario.business.dto.request.UsuarioUpdateRequest;
import com.javanauta.usuario.business.dto.response.EnderecoResponse;
import com.javanauta.usuario.business.dto.response.TelefoneResponse;
import com.javanauta.usuario.business.dto.response.UsuarioResponse;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .enderecos(request.getEnderecos() != null ?
                        paraListaEnderecos(request.getEnderecos()) : null)
                .telefones(request.getTelefones() != null ?
                        paraListaTelefones(request.getTelefones()) : null)
                .build();
    }

    public List<Endereco> paraListaEnderecos(List<EnderecoRequest> requests) {
        return requests.stream()
                .map(this::paraEndereco)
                .toList();
    }

    public Endereco paraEndereco(EnderecoRequest request) {
        return Endereco.builder()
                .rua(request.getRua())
                .numero(request.getNumero())
                .cidade(request.getCidade())
                .complemento(request.getComplemento())
                .cep(request.getCep())
                .estado(request.getEstado())
                .build();
    }

    public List<Telefone> paraListaTelefones(List<TelefoneRequest> requests) {
        return requests.stream()
                .map(this::paraTelefone)
                .toList();
    }

    public Telefone paraTelefone(TelefoneRequest request) {
        return Telefone.builder()
                .numero(request.getNumero())
                .ddd(request.getDdd())
                .build();
    }

    public UsuarioResponse paraUsuarioResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(usuario.getEnderecos() != null ?
                        paraListaEnderecosResponse(usuario.getEnderecos()) : null)
                .telefones(usuario.getTelefones() != null ?
                        paraListaTelefonesResponse(usuario.getTelefones()) : null)
                .build();
    }

    public List<EnderecoResponse> paraListaEnderecosResponse(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::paraEnderecoResponse)
                .toList();
    }

    public EnderecoResponse paraEnderecoResponse(Endereco endereco) {
        return EnderecoResponse.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    public List<TelefoneResponse> paraListaTelefonesResponse(List<Telefone> telefones) {
        return telefones.stream()
                .map(this::paraTelefoneResponse)
                .toList();
    }

    public TelefoneResponse paraTelefoneResponse(Telefone telefone) {
        return TelefoneResponse.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioUpdateRequest request, Usuario entity) {
        return Usuario.builder()
                .id(entity.getId())
                .nome(request.getNome() != null ? request.getNome() : entity.getNome())
                .email(request.getEmail() != null ? request.getEmail() : entity.getEmail())
                .senha(request.getSenha() != null ? request.getSenha() : entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco updateEndereco(EnderecoRequest request, Endereco entity) {
        return Endereco.builder()
                .id(entity.getId())
                .rua(request.getRua() != null ? request.getRua() : entity.getRua())
                .numero(request.getNumero() != null ? request.getNumero() : entity.getNumero())
                .complemento(request.getComplemento() != null ? request.getComplemento() : entity.getComplemento())
                .cidade(request.getCidade() != null ? request.getCidade() : entity.getCidade())
                .cep(request.getCep() != null ? request.getCep() : entity.getCep())
                .estado(request.getEstado() != null ? request.getEstado() : entity.getEstado())
                .usuario_id(entity.getUsuario_id())
                .build();
    }

    public Telefone updateTelefone(TelefoneRequest request, Telefone entity) {
        return Telefone.builder()
                .id(entity.getId())
                .numero(request.getNumero() != null ? request.getNumero() : entity.getNumero())
                .ddd(request.getDdd() != null ? request.getDdd() : entity.getDdd())
                .usuario_id(entity.getUsuario_id())
                .build();
    }

    public Endereco paraEnderecoEntity(EnderecoRequest request, Long idUsuario) {
        return Endereco.builder()
                .rua(request.getRua())
                .numero(request.getNumero())
                .cidade(request.getCidade())
                .complemento(request.getComplemento())
                .cep(request.getCep())
                .estado(request.getEstado())
                .usuario_id(idUsuario)
                .build();
    }

    public Telefone paraTelefoneEntity(TelefoneRequest request, Long idUsuario) {
        return Telefone.builder()
                .numero(request.getNumero())
                .ddd(request.getDdd())
                .usuario_id(idUsuario)
                .build();
    }

}
