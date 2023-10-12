package br.com.sanittas.app.service;

import br.com.sanittas.app.model.Empresa;
import br.com.sanittas.app.model.Endereco;
import br.com.sanittas.app.repository.EmpresaRepository;
import br.com.sanittas.app.service.empresa.dto.EmpresaCriacaoDto;
import br.com.sanittas.app.service.empresa.dto.ListaEmpresa;
import br.com.sanittas.app.service.endereco.dto.ListaEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpresaServices {
    @Autowired
    EmpresaRepository repository;

    public List<ListaEmpresa> listarEmpresas() {
        List<Empresa> empresas = repository.findAll();
        List<ListaEmpresa> listaEmpresas = new ArrayList<>();
        for (Empresa empresa : empresas) {
            List<ListaEndereco> listaEnderecos = new ArrayList<>();
            extrairEndereco(empresa, listaEnderecos);
            var empresaDto = new ListaEmpresa(
                    empresa.getId(),
                    empresa.getRazaoSocial(),
                    empresa.getCnpj(),
                    listaEnderecos
            );
            listaEmpresas.add(empresaDto);
        }
        return listaEmpresas;
    }

    private static void extrairEndereco(Empresa empresa, List<ListaEndereco> listaEnderecos) {
        for(Endereco endereco : empresa.getEnderecos()){
            var enderecoDto = new ListaEndereco(
                    endereco.getId(),
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getEstado(),
                    endereco.getCidade()
            );
            listaEnderecos.add(enderecoDto);
        }
    }

    public void cadastrar(EmpresaCriacaoDto empresa) {
        Empresa empresaNova = new Empresa();
        empresaNova.setRazaoSocial(empresa.razaoSocial());
        empresaNova.setCnpj(empresa.cnpj());

        repository.save(empresaNova);
    }

    public void atualizar(EmpresaCriacaoDto empresa, Integer id) {
        var empresaAtualizada = repository.findById(id);
        if (empresaAtualizada.isPresent()){
            empresaAtualizada.get().setRazaoSocial(empresa.razaoSocial());
            empresaAtualizada.get().setCnpj(empresa.cnpj());
            repository.save(empresaAtualizada.get());
        }
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}
