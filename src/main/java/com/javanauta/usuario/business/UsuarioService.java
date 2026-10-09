package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
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
import com.javanauta.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioResponse salvaUsuario(UsuarioRequest request) {
        emailExiste(request.getEmail());
        request.setSenha(passwordEncoder.encode(request.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(request);
        return usuarioConverter.paraUsuarioResponse(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe) {
                throw new ConflictException("Email já existe");
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioResponse buscarUsuarioPorEmail(String email) {
        try {
               Usuario usuario = usuarioRepository.findByEmail(email)
                       .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));
               return usuarioConverter.paraUsuarioResponse(usuario);
        } catch(ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }


    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioResponse atualizaDadosUsuario(String token, UsuarioUpdateRequest request) {
        String email = jwtUtil.extractEmailToken(token.substring(7));

        request.setSenha(request.getSenha() != null ? passwordEncoder.encode(request.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));

        Usuario usuario = usuarioConverter.updateUsuario(request, usuarioEntity);

        return usuarioConverter.paraUsuarioResponse(usuarioRepository.save(usuario));
    }

    public EnderecoResponse atualizaEndereco(Long idEndereco, EnderecoRequest request) {
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado " + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(request, entity);

        return usuarioConverter.paraEnderecoResponse(enderecoRepository.save(endereco));
    }

    public TelefoneResponse atualizaTelefone(Long idTelefone, TelefoneRequest request) {
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado  " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(request, entity);

        return usuarioConverter.paraTelefoneResponse(telefoneRepository.save(telefone));
    }

    public EnderecoResponse cadastraEndereco(String token, EnderecoRequest request) {
        String email = jwtUtil.extractEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(request, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoResponse(enderecoEntity);
    }

    public TelefoneResponse cadastraTelefone(String token, TelefoneRequest request) {
        String email = jwtUtil.extractEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(request, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneResponse(telefoneEntity);
    }

}
