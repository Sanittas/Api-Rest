package br.com.sanittas.app.controller;

import br.com.sanittas.app.controller.request.DadosUsuario;
import br.com.sanittas.app.exception.ValidacaoException;
import br.com.sanittas.app.model.Usuario;
import br.com.sanittas.app.services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServices services;

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> listar() {
       var response = services.listarUsuarios();
       if (!response.isEmpty()){
           return ResponseEntity.status(200).body(response);
       }
       return ResponseEntity.status(204).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try{
            var usuario = services.buscar(id);
            return ResponseEntity.status(200).body(usuario);
        }catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> cadastrar(@RequestBody DadosUsuario dados) {
        try{
            var usuario = services.cadastrar(dados);
            return ResponseEntity.status(201).body(usuario);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody DadosUsuario dados) {
        try{
            var usuario = services.atualizar(id,dados);
            return ResponseEntity.status(200).body(usuario);
        }catch (ValidacaoException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try{
            services.deletar(id);
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }
}
