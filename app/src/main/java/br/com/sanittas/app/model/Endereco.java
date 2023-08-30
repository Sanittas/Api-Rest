package br.com.sanittas.app.model;

import br.com.sanittas.app.controller.request.DadosEndereco;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class Endereco {
    @NotBlank
    private String rua;
    @NotBlank
    private String numero;
    private String complemento;
    @NotBlank
    private String estado;
    @NotBlank
    private String cidade;

    public Endereco() {

    }

    public Endereco(DadosEndereco dados) {
        this.rua = dados.rua();
        this.numero = dados.numero();
        this.complemento = dados.complemento();
        this.estado = dados.estado();
        this.cidade = dados.cidade();
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
