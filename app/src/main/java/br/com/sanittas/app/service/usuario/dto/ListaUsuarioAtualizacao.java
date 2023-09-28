package br.com.sanittas.app.service.usuario.dto;

public record ListaUsuarioAtualizacao(
        Long id,
        String nome,
        String email,
        String cpf,
        String celular,
        String senha
) {
}
