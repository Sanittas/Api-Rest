package br.com.sanittas.app.repository;

import br.com.sanittas.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String username);

//    @Override
//    @Query(value = "SELECT u FROM Usuario u ")
//    List<Usuario> findAll();
}
