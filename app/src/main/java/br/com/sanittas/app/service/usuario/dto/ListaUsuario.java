package br.com.sanittas.app.service.usuario.dto;

import br.com.sanittas.app.service.endereco.dto.ListaEndereco;

import java.util.List;

public record ListaUsuario(
        Long id,
        String nome,
        String email,
        String cpf,
        String celular,
        String senha,
        List<ListaEndereco> enderecos
) {

}
