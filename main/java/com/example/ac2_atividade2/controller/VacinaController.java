package com.example.ac2_atividade2.controller;

import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.ac2_atividade2.dto.VacinaRequest;
import com.example.ac2_atividade2.model.Vacina;
import com.example.ac2_atividade2.service.ProntuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacinas")
public class VacinaController {

    private final ProntuarioService prontuarioService;

    public VacinaController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vacina registrar(@RequestBody VacinaRequest request) {
        return prontuarioService.registrarVacina(request);
    }

    @GetMapping
    public List<Vacina> aplicadasNoPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return prontuarioService.vacinasNoPeriodo(inicio, fim);
    }
}
