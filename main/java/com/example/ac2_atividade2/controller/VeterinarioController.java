package com.example.ac2_atividade2.controller;

import com.example.ac2_atividade2.model.Veterinario;
import com.example.ac2_atividade2.service.VeterinarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    public VeterinarioController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Veterinario criar(@RequestBody Veterinario veterinario) {
        return veterinarioService.criar(veterinario);
    }

    @GetMapping
    public List<Veterinario> listar() {
        return veterinarioService.listar();
    }

    @GetMapping("/{id}")
    public Veterinario buscar(@PathVariable Long id) {
        return veterinarioService.buscar(id);
    }

    @PutMapping("/{id}")
    public Veterinario atualizar(@PathVariable Long id, @RequestBody Veterinario veterinario) {
        return veterinarioService.atualizar(id, veterinario);
    }

    @GetMapping("/especie/{especie}")
    public List<Veterinario> porEspecie(@PathVariable String especie) {
        return veterinarioService.listarPorEspecie(especie);
    }
}
