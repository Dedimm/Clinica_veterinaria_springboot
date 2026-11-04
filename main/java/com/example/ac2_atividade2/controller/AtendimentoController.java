package com.example.ac2_atividade2.controller;

import com.example.ac2_atividade2.dto.AtendimentoRequest;
import com.example.ac2_atividade2.model.Atendimento;
import com.example.ac2_atividade2.service.ProntuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final ProntuarioService prontuarioService;

    public AtendimentoController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Atendimento registrar(@RequestBody AtendimentoRequest request) {
        return prontuarioService.registrarAtendimento(request);
    }

    @GetMapping("/{id}")
    public Atendimento buscar(@PathVariable Long id) {
        return prontuarioService.buscarAtendimento(id);
    }

    @GetMapping(value = "/{id}/imprimir", produces = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
    public String imprimir(@PathVariable Long id) {
        return prontuarioService.gerarHtmlParaImpressao(id);
    }
}
