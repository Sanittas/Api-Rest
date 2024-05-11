package com.sanittas.AuthServer.services;

import com.sanittas.AuthServer.controller.LoginDtoRequest;
import com.sanittas.AuthServer.controller.LoginDtoResponse;
import com.sanittas.AuthServer.controller.UsuarioCriacaoDto;
import com.sanittas.AuthServer.domain.Usuario;
import com.sanittas.AuthServer.domain.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public LoginDtoResponse login(LoginDtoRequest loginDto) {
        Usuario usuario = repository.findByEmail(loginDto.username()).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));
        if (!passwordEncoder.matches(loginDto.password(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha incorreta");
        }
        Date expiryDate = new Date(System.currentTimeMillis() +7200000);
        String token = AuthService.generateToken(usuario, expiryDate);
        return new LoginDtoResponse(usuario.getId(), usuario.getUsername(), token);
    }

    public Usuario cadastrar(UsuarioCriacaoDto usuarioCriacaoDto) {
        log.info("Cadastrando novo usuário");

        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);

        if (repository.existsByEmail(novoUsuario.getEmail())) {
            log.error("Email já cadastrado!");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (repository.existsByCpf(novoUsuario.getCpf())) {
            log.error("CPF já cadastrado!");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        novoUsuario.setSenha(passwordEncoder.encode(usuarioCriacaoDto.getSenha()));
        return repository.save(novoUsuario);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(username).orElseThrow( () -> new UsernameNotFoundException("Usuário não encontrado"));
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return usuario;
    }
}
