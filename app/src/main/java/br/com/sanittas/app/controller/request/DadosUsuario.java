package br.com.sanittas.app.controller.request;

public record DadosUsuario(
        String nome,
        String cpf,
        String celular,
        String senha,
        DadosEndereco endereco
) {
}
