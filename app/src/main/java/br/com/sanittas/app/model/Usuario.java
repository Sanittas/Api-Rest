package br.com.sanittas.app.model;

import br.com.sanittas.app.controller.request.DadosEndereco;
import br.com.sanittas.app.controller.request.DadosUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Table(name="Usuarios")
@Entity(name="Usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String cpf;
    @NotBlank
    private String celular;
    @NotBlank
    private String senha;
    @Embedded
    private Endereco endereco;

    public Usuario() {

    }
    public Usuario(DadosUsuario dados) {
        this.nome = dados.nome();
        this.celular = dados.celular();
        this.cpf = dados.cpf();
        this.senha = dados.senha();
        this.endereco = new Endereco(dados.endereco());
    }

    public Long getId() {
        return id;
    }

    public void atualizarEndereco(DadosEndereco dados) {
        if (!dados.rua().isEmpty()) {
            this.endereco.setRua(dados.rua());
        }
        if (!dados.numero().isEmpty()) {
            this.endereco.setNumero(dados.numero());
        }
        if (!dados.complemento().isEmpty()) {
            this.endereco.setComplemento(dados.complemento());
        }
        if (!dados.estado().isEmpty()) {
            this.endereco.setEstado(dados.estado());
        }
        if (!dados.cidade().isEmpty()) {
            this.endereco.setCidade(dados.cidade());
        }

    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCelular() {
        return celular;
    }

    public String getSenha() {
        return senha;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
