package br.com.sanittas.app.controller.request;

public record DadosEndereco(
        String rua,
        String numero,
        String complemento,
        String estado,
        String cidade
) {
}
