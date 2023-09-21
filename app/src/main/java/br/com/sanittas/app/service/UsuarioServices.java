package br.com.sanittas.app.service;

import br.com.sanittas.app.api.configuration.security.jwt.GerenciadorTokenJwt;
import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.usuario.dto.UsuarioCriacaoDto;
import br.com.sanittas.app.service.usuario.dto.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@Service
public class UsuarioServices {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public void cadastrar(UsuarioCriacaoDto usuarioCriacaoDto) {
        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        repository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado =
                repository.findByEmail(usuarioLoginDto.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        var usuario = repository.findById(id);
        if (usuario.isPresent()){
            usuario.get().setNome(dados.getNome());
            usuario.get().setEmail(dados.getEmail());
            usuario.get().setCpf(dados.getCpf());
            usuario.get().setCelular(dados.getCelular());
            usuario.get().setSenha(dados.getSenha());
            usuario.get().setEndereco(dados.getEndereco());
            repository.save(usuario.get());
            return usuario.get();
        }
        return null;
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ValidacaoException("Usuário não existe!");
        }
        repository.deleteById(id);
    }

    public Usuario buscar(Long id) {
        var usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new ValidacaoException("Usuário não existe!");
        }
        return usuario.get();
    }
}
