package com.example.ac2_atividade2.service;

import com.example.ac2_atividade2.dto.AnimalRequest;
import com.example.ac2_atividade2.exception.RecursoNaoEncontradoException;
import com.example.ac2_atividade2.model.Animal;
import com.example.ac2_atividade2.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final TutorService tutorService;

    public AnimalService(AnimalRepository animalRepository, TutorService tutorService) {
        this.animalRepository = animalRepository;
        this.tutorService = tutorService;
    }

    public List<Animal> listar() {
        return animalRepository.findAll();
    }

    public List<Animal> listarPorTutor(Long tutorId) {
        tutorService.buscar(tutorId);
        return animalRepository.findByTutorId(tutorId);
    }

    public Animal buscar(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal " + id + " não encontrado."));
    }

    public Animal criar(AnimalRequest request) {
        return animalRepository.save(preencher(new Animal(), request));
    }

    public Animal atualizar(Long id, AnimalRequest request) {
        return animalRepository.save(preencher(buscar(id), request));
    }

    private Animal preencher(Animal animal, AnimalRequest r) {
        animal.setNome(Validacao.texto(r.nome(), "nome"));
        animal.setEspecie(Validacao.texto(r.especie(), "especie"));
        animal.setRaca(r.raca());
        animal.setDataNascimento(Validacao.obrigatorio(r.dataNascimento(), "dataNascimento"));
        animal.setTutor(tutorService.buscar(Validacao.obrigatorio(r.tutorId(), "tutorId")));
        return animal;
    }

    public List<Animal> buscarPorNome(String nome) {
        return animalRepository.buscarPorNome(Validacao.texto(nome, "nome"));
    }
}
