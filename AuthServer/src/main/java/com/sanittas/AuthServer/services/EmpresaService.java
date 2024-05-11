package com.sanittas.AuthServer.services;

import com.sanittas.AuthServer.controller.EmpresaCriacaoDto;
import com.sanittas.AuthServer.controller.LoginDtoRequest;
import com.sanittas.AuthServer.controller.LoginDtoResponse;
import com.sanittas.AuthServer.domain.Empresa;
import com.sanittas.AuthServer.domain.EmpresaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class EmpresaService {
    private final EmpresaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public LoginDtoResponse login(LoginDtoRequest loginDto) {
        Empresa empresa = repository.findByCnpj(loginDto.username()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));
        if (!passwordEncoder.matches(loginDto.password(), empresa.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha incorreta");
        }
        Date expiryDate = new Date(System.currentTimeMillis() +7200000);
        String token = AuthService.generateToken(empresa, expiryDate);
        return new LoginDtoResponse(empresa.getId(), empresa.getUsername(), token);

    }

    public void cadastrar(EmpresaCriacaoDto empresa) {
        if (repository.existsByRazaoSocial(empresa.razaoSocial())) {
            log.error("Razão social já cadastrada");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Razão social já cadastrada");
        }
        if (repository.existsByCnpj(empresa.cnpj())) {
            log.error("CNPJ já cadastrado");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado");
        }
        if (repository.findByEmail(empresa.email()).isPresent()) {
            log.error("Email já cadastrado");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        log.info("Cadastrando nova empresa.");
        Empresa empresaNova = new Empresa();
        empresaNova.setRazaoSocial(empresa.razaoSocial());
        empresaNova.setCnpj(empresa.cnpj());
        empresaNova.setEmail(empresa.email());
        empresaNova.setSenha(passwordEncoder.encode(empresa.senha()));
        repository.save(empresaNova);
        log.info("Empresa cadastrada com sucesso.");
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Empresa empresa = repository.findByCnpj(username).orElseThrow(() -> new UsernameNotFoundException("Empresa não encontrada"));
        if (empresa == null) {
            throw new UsernameNotFoundException("Empresa não encontrada");
        }
        return empresa;
    }
}
