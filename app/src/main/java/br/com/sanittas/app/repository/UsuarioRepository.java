package br.com.sanittas.app.repository;

import br.com.sanittas.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends   JpaRepository<Usuario, Long> {

}
