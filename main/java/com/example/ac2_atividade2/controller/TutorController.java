package com.example.ac2_atividade2.controller;

import com.example.ac2_atividade2.model.Animal;
import com.example.ac2_atividade2.model.Tutor;
import com.example.ac2_atividade2.service.AnimalService;
import com.example.ac2_atividade2.service.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    private final TutorService tutorService;
    private final AnimalService animalService;

    public TutorController(TutorService tutorService, AnimalService animalService) {
        this.tutorService = tutorService;
        this.animalService = animalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tutor criar(@RequestBody Tutor tutor) {
        return tutorService.criar(tutor);
    }

    @GetMapping
    public List<Tutor> listar() {
        return tutorService.listar();
    }

    @GetMapping("/{id}")
    public Tutor buscar(@PathVariable Long id) {
        return tutorService.buscar(id);
    }

    @PutMapping("/{id}")
    public Tutor atualizar(@PathVariable Long id, @RequestBody Tutor tutor) {
        return tutorService.atualizar(id, tutor);
    }

    @GetMapping("/{id}/animais")
    public List<Animal> animaisDoTutor(@PathVariable Long id) {
        return animalService.listarPorTutor(id);
    }

    @GetMapping("/com-animais")
    public List<Tutor> comPeloMenosAnimais(@RequestParam(defaultValue = "2") long minimo) {
        return tutorService.comPeloMenosAnimais(minimo);
    }
}
