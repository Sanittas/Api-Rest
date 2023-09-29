package br.com.sanittas.app.service;

import br.com.sanittas.app.api.configuration.security.jwt.GerenciadorTokenJwt;
import br.com.sanittas.app.api.configuration.security.token.Token;
import br.com.sanittas.app.api.configuration.security.token.TokenRepository;
import br.com.sanittas.app.api.configuration.security.token.TokenType;
import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioLoginDto;
import br.com.sanittas.app.service.autenticacao.dto.UsuarioTokenDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import br.com.sanittas.app.service.usuario.dto.ListaUsuario;
import br.com.sanittas.app.service.usuario.dto.ListaUsuarioAtualizacao;
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

import java.util.ArrayList;
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
    @Autowired
    private TokenRepository tokenRepository;

    public List<ListaUsuario> listarUsuarios() {
        var usuarios = repository.findAll();
        List<ListaUsuario> listaUsuarios = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            List<ListaEndereco> listaEnderecos = new ArrayList<>();
            for(Endereco endereco : usuario.getEnderecos()){
                       var enderecoDto = new ListaEndereco(
                               endereco.getId(),
                               endereco.getLogradouro(),
                               endereco.getNumero(),
                               endereco.getComplemento(),
                               endereco.getEstado(),
                               endereco.getCidade()
                       );
                          listaEnderecos.add(enderecoDto);
            }
            var usuarioDto = new ListaUsuario(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getCpf(),
                    usuario.getCelular(),
                    usuario.getSenha(),
                    listaEnderecos
            );
            listaUsuarios.add(usuarioDto);
        }
        return listaUsuarios;
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

        final String jwtToken = gerenciadorTokenJwt.generateToken(authentication);
        var token = Token.builder()
                .usuario(usuarioAutenticado)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        revokeAllUserTokens(usuarioAutenticado);
        tokenRepository.save(token);

        return UsuarioMapper.of(usuarioAutenticado, token.getToken());
    }

    private void revokeAllUserTokens(Usuario usuario) {
        var validTokens = tokenRepository.findAllValidTokensByUsuarioId(usuario.getId());
        if (validTokens.isEmpty())
            return;
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    public ListaUsuarioAtualizacao atualizar(Integer id, Usuario dados) {
        var usuario = repository.findById(id);
        if (usuario.isPresent()){
            usuario.get().setNome(dados.getNome());
            usuario.get().setEmail(dados.getEmail());
            usuario.get().setCpf(dados.getCpf());
            usuario.get().setCelular(dados.getCelular());
            usuario.get().setSenha(dados.getSenha());
            ListaUsuarioAtualizacao usuarioDto = new ListaUsuarioAtualizacao(
                    usuario.get().getId(),
                    usuario.get().getNome(),
                    usuario.get().getEmail(),
                    usuario.get().getCpf(),
                    usuario.get().getCelular(),
                    usuario.get().getSenha()
            );
            repository.save(usuario.get());
            return usuarioDto;
        }
        return null;
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ValidacaoException("Usuário não existe!");
        }
        repository.deleteById(id);
    }

    public ListaUsuario buscar(Integer id) {
        var usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new ValidacaoException("Usuário não existe!");
        }
            List<ListaEndereco> listaEnderecos = new ArrayList<>();
            for(Endereco endereco : usuario.get().getEnderecos()){
                var enderecoDto = new ListaEndereco(
                        endereco.getId(),
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        endereco.getEstado(),
                        endereco.getCidade()
                );
                listaEnderecos.add(enderecoDto);
            }
            ListaUsuario usuarioDto = new ListaUsuario(
                    usuario.get().getId(),
                    usuario.get().getNome(),
                    usuario.get().getEmail(),
                    usuario.get().getCpf(),
                    usuario.get().getCelular(),
                    usuario.get().getSenha(),
                    listaEnderecos
            );
        return usuarioDto;
    }

    public Token recuperarToken(String jwtToken) {
        return tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new ResponseStatusException(404, "Token não encontrado", null));
    }
}