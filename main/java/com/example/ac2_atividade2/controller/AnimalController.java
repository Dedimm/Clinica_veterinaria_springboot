package com.example.ac2_atividade2.controller;

import com.example.ac2_atividade2.dto.AnimalRequest;
import com.example.ac2_atividade2.dto.HistoricoAnimal;
import com.example.ac2_atividade2.model.Animal;
import com.example.ac2_atividade2.model.Atendimento;
import com.example.ac2_atividade2.model.Vacina;
import com.example.ac2_atividade2.service.AnimalService;
import com.example.ac2_atividade2.service.ProntuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;
    private final ProntuarioService prontuarioService;

    public AnimalController(AnimalService animalService, ProntuarioService prontuarioService) {
        this.animalService = animalService;
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Animal criar(@RequestBody AnimalRequest request) {
        return animalService.criar(request);
    }

    @GetMapping
    public List<Animal> listar() {
        return animalService.listar();
    }

    @GetMapping("/{id}")
    public Animal buscar(@PathVariable Long id) {
        return animalService.buscar(id);
    }

    @PutMapping("/{id}")
    public Animal atualizar(@PathVariable Long id, @RequestBody AnimalRequest request) {
        return animalService.atualizar(id, request);
    }

    @GetMapping("/{id}/atendimentos")
    public List<Atendimento> atendimentos(@PathVariable Long id) {
        return prontuarioService.atendimentosDoAnimal(id);
    }

    @GetMapping("/{id}/vacinas")
    public List<Vacina> vacinas(@PathVariable Long id) {
        return prontuarioService.vacinasDoAnimal(id);
    }

    @GetMapping("/{id}/historico")
    public HistoricoAnimal historico(@PathVariable Long id) {
        return prontuarioService.historico(id);
    }

    @GetMapping("/busca")
    public List<Animal> buscarPorNome(@RequestParam String nome) {
        return animalService.buscarPorNome(nome);
    }
}
