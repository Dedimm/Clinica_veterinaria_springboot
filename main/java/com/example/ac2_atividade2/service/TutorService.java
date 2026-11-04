package com.example.ac2_atividade2.service;

import com.example.ac2_atividade2.exception.RecursoNaoEncontradoException;
import com.example.ac2_atividade2.exception.RegraNegocioException;
import com.example.ac2_atividade2.model.Tutor;
import com.example.ac2_atividade2.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<Tutor> listar() {
        return tutorRepository.findAll();
    }

    public Tutor buscar(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor " + id + " não encontrado."));
    }

    public Tutor criar(Tutor dados) {
        Tutor tutor = new Tutor();
        preencher(tutor, dados);
        if (tutorRepository.existsByCpf(tutor.getCpf())) {
            throw new RegraNegocioException("Já existe um tutor com este CPF.");
        }
        return tutorRepository.save(tutor);
    }

    public Tutor atualizar(Long id, Tutor dados) {
        Tutor tutor = buscar(id);
        preencher(tutor, dados);
        if (tutorRepository.existsByCpfAndIdNot(tutor.getCpf(), id)) {
            throw new RegraNegocioException("Já existe outro tutor com este CPF.");
        }
        return tutorRepository.save(tutor);
    }

    private void preencher(Tutor destino, Tutor dados) {
        destino.setNome(Validacao.texto(dados.getNome(), "nome"));
        String cpf = Validacao.texto(dados.getCpf(), "cpf").replaceAll("\\D", "");
        if (cpf.length() != 11) {
            throw new RegraNegocioException("O CPF deve conter 11 dígitos.");
        }
        destino.setCpf(cpf);
        destino.setTelefone(Validacao.texto(dados.getTelefone(), "telefone"));
        destino.setEndereco(Validacao.texto(dados.getEndereco(), "endereco"));
    }

    public List<Tutor> comPeloMenosAnimais(long minimo) {
        if (minimo < 1) {
            throw new RegraNegocioException("O mínimo de animais deve ser pelo menos 1.");
        }
        return tutorRepository.comPeloMenosAnimais(minimo);
    }
}
