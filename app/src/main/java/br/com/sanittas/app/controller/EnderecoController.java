package br.com.sanittas.app.controller;

import br.com.sanittas.app.service.EnderecoServices;
import br.com.sanittas.app.service.endereco.dto.EnderecoCriacaoDto;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoServices services;

    @GetMapping("/{id_usuario}")
    public ResponseEntity<List<ListaEndereco>> listarEnderecosPorUsuario(@PathVariable Long id_usuario) {
        try{
            var response = services.listarEnderecosPorUsuario(id_usuario);
            if (!response.isEmpty()) {
                return ResponseEntity.status(200).body(response);
            }
            return ResponseEntity.status(204).build();
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/{usuario_id}")
    public ResponseEntity<Void> cadastrarEndereco(@RequestBody EnderecoCriacaoDto endereco, @PathVariable Long usuario_id) {
        try {
            services.cadastrar(endereco,usuario_id);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaEndereco> atualizarEndereco(@RequestBody EnderecoCriacaoDto enderecoCriacaoDto,@PathVariable Long id) {
        try{
            var endereco = services.atualizar(enderecoCriacaoDto, id);
            return ResponseEntity.status(200).body(endereco);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id) {
        try{
            services.deletar(id);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(400).build();
        }
    }

}

