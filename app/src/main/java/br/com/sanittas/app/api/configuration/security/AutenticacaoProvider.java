package br.com.sanittas.app.api.configuration.security;

import br.com.sanittas.app.service.autenticacao.dto.AutenticacaoService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

public class AutenticacaoProvider implements AuthenticationProvider {

    private static final Logger LOGGER = Logger.getLogger(AutenticacaoProvider.class.getName());
    private final AutenticacaoService usuarioAutorizacaoService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AutenticacaoProvider(AutenticacaoService usuarioAutorizacaoService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioAutorizacaoService = usuarioAutorizacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        LOGGER.info("Autenticando usuário: " + username);
        final String password = authentication.getCredentials().toString();
        LOGGER.info("Autenticando senha: " + password);

        UserDetails userDetails = this.usuarioAutorizacaoService.loadUserByUsername(username);
        if (this.passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}