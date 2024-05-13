package com.sanittas.AuthServer.controller;

import com.sanittas.AuthServer.services.EmpresaService;
import com.sanittas.AuthServer.services.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
public class AuthController {
    private final UsuarioService usuarioService;
    private final EmpresaService empresaService;

    @PostMapping("/login/usuario")
    public ResponseEntity<LoginDtoResponse> loginUsuario(@RequestBody LoginDtoRequest loginDto) {
        try{
        LoginDtoResponse response = usuarioService.login(loginDto);
        return ResponseEntity.ok(response);
        }
        catch (ResponseStatusException e){
            throw new ResponseStatusException(e.getStatusCode(), e.getReason());
        }
    }

    @PostMapping("/login/empresa")
    public ResponseEntity<LoginDtoResponse> loginEmpresa(@RequestBody LoginDtoRequest loginDto) {
        try{
        LoginDtoResponse response = empresaService.login(loginDto);
        return ResponseEntity.ok(response);
        }
        catch (ResponseStatusException e){
            throw new ResponseStatusException(e.getStatusCode(), e.getReason());
        }
    }

}
