package com.sanittas.AuthServer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String username);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
