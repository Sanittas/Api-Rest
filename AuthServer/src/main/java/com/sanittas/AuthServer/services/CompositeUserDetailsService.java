package com.sanittas.AuthServer.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CompositeUserDetailsService implements UserDetailsService {
    private final UsuarioService usuarioService;
    private final EmpresaService empresaService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            return usuarioService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return empresaService.loadUserByUsername(username);
        }
    }
}
