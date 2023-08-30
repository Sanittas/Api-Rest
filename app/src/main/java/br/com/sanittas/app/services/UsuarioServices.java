package br.com.sanittas.app.services;

import br.com.sanittas.app.controller.request.DadosUsuario;
import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.util.List;

@Service
public class UsuarioServices {
    @Autowired
    private UsuarioRepository repository;
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Usuario cadastrar(DadosUsuario dados) {
        Pattern patternCpf = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
        Matcher matcher = patternCpf.matcher(dados.cpf());
        boolean matchFound = matcher.find();
        if (!matchFound){
            throw new ValidacaoException("CPF inválido!");
        }
        Pattern patternCelular = Pattern.compile("^\\(\\d{2}\\)9\\d{4}-\\d{4}$");
        matcher = patternCelular.matcher(dados.celular());
        matchFound = matcher.find();
        if (!matchFound){
            throw new ValidacaoException("Número de celular inválido!");
        }
        var novoUsuario = new Usuario(dados);
        repository.save(novoUsuario);
        return novoUsuario;
    }

    public Usuario atualizar(Long id, DadosUsuario dados) {
        var usuario = repository.findById(id);
        Pattern patternCpf = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
        Matcher matcher = patternCpf.matcher(dados.cpf());
        boolean matchFound = matcher.find();
        if (!matchFound){
            throw new ValidacaoException("CPF inválido!");
        }
        Pattern patternCelular = Pattern.compile("^\\(\\d{2}\\)9\\d{4}-\\d{4}$");
        matcher = patternCelular.matcher(dados.celular());
        matchFound = matcher.find();
        if (!matchFound){
            throw new ValidacaoException("Número de celular inválido!");
        }
        if (!(dados.nome() == null)){
            usuario.get().setNome(dados.nome());
        }
        usuario.get().setCpf(dados.cpf());
        usuario.get().setCelular(dados.celular());
        if (!(dados.senha() == null)){
            usuario.get().setSenha(dados.senha());
        }

        repository.save(usuario.get());
        return usuario.get();
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
