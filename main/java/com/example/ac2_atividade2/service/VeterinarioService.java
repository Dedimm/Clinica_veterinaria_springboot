package com.example.ac2_atividade2.service;

import com.example.ac2_atividade2.exception.RecursoNaoEncontradoException;
import com.example.ac2_atividade2.exception.RegraNegocioException;
import com.example.ac2_atividade2.model.Veterinario;
import com.example.ac2_atividade2.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    public List<Veterinario> listar() {
        return veterinarioRepository.findAll();
    }

    public Veterinario buscar(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário " + id + " não encontrado."));
    }

    public Veterinario criar(Veterinario dados) {
        Veterinario vet = new Veterinario();
        preencher(vet, dados);
        if (veterinarioRepository.existsByCrmv(vet.getCrmv())) {
            throw new RegraNegocioException("Já existe um veterinário com este CRMV.");
        }
        return veterinarioRepository.save(vet);
    }

    public Veterinario atualizar(Long id, Veterinario dados) {
        Veterinario vet = buscar(id);
        preencher(vet, dados);
        if (veterinarioRepository.existsByCrmvAndIdNot(vet.getCrmv(), id)) {
            throw new RegraNegocioException("Já existe outro veterinário com este CRMV.");
        }
        return veterinarioRepository.save(vet);
    }

    private void preencher(Veterinario destino, Veterinario dados) {
        destino.setNome(Validacao.texto(dados.getNome(), "nome"));
        destino.setCrmv(Validacao.texto(dados.getCrmv(), "crmv").toUpperCase());
        if (dados.getEspecies() == null || dados.getEspecies().isEmpty()) {
            throw new RegraNegocioException("Informe ao menos uma espécie que o veterinário pode atender.");
        }
        Set<String> especies = new HashSet<>();
        for (String e : dados.getEspecies()) {
            especies.add(Validacao.normalizar(Validacao.texto(e, "especies")));
        }
        destino.setEspecies(especies);
    }

    public List<Veterinario> listarPorEspecie(String especie) {
        return veterinarioRepository.buscarPorEspecie(Validacao.normalizar(Validacao.texto(especie, "especie")));
    }
}
