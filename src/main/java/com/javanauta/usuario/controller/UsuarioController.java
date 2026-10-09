package com.javanauta.usuario.controller;

import com.javanauta.usuario.business.UsuarioService;
import com.javanauta.usuario.business.ViaCepService;
import com.javanauta.usuario.business.dto.request.EnderecoRequest;
import com.javanauta.usuario.business.dto.request.LoginRequest;
import com.javanauta.usuario.business.dto.request.TelefoneRequest;
import com.javanauta.usuario.business.dto.request.UsuarioRequest;
import com.javanauta.usuario.business.dto.request.UsuarioUpdateRequest;
import com.javanauta.usuario.business.dto.response.EnderecoResponse;
import com.javanauta.usuario.business.dto.response.TelefoneResponse;
import com.javanauta.usuario.business.dto.response.UsuarioResponse;
import com.javanauta.usuario.infrastructure.clients.ViaCepResponse;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import com.javanauta.usuario.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Cadastro e login de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ViaCepService viaCepService;

    @PostMapping
    @Operation(summary = "Salvar Usuários", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro do servidor")
    public ResponseEntity<UsuarioResponse> salvaUsuario(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(request));
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioResponse> atualizaDadosUsuario(@RequestBody UsuarioUpdateRequest request,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, request));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoResponse> atualizaEndereco(@RequestBody EnderecoRequest request,
                                                             @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, request));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneResponse> atualizaTelefone(@RequestBody TelefoneRequest request,
                                                             @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, request));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoResponse> cadastraEndereco(@RequestBody EnderecoRequest request,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, request));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneResponse> cadastraTelefone(@RequestBody TelefoneRequest request,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, request));
    }

    @GetMapping("/endereco/{cep}")
    public ResponseEntity<ViaCepResponse> buscarEnderecoPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepService.buscarDadosEndereco(cep));
    }

}
