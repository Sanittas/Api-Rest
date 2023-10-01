package br.com.sanittas.app.service.autenticacao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioLoginDto {
    private String email;
    private String senha;

}

