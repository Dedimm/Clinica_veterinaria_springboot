package com.example.ac2_atividade2.controller;

import com.example.ac2_atividade2.model.StatusConsulta;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.ac2_atividade2.dto.ConsultaRequest;
import com.example.ac2_atividade2.model.Consulta;
import com.example.ac2_atividade2.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consulta agendar(@RequestBody ConsultaRequest request) {
        return consultaService.agendar(request);
    }

    @GetMapping
    public List<Consulta> listar(@RequestParam(required = false) Long animalId,
                                 @RequestParam(required = false) Long veterinarioId) {
        return consultaService.listar(animalId, veterinarioId);
    }

    @GetMapping("/{id}")
    public Consulta buscar(@PathVariable Long id) {
        return consultaService.buscar(id);
    }

    @PutMapping("/{id}/cancelar")
    public Consulta cancelar(@PathVariable Long id) {
        return consultaService.cancelar(id);
    }

    @GetMapping("/agenda")
    public List<Consulta> agenda(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) StatusConsulta status) {
        return consultaService.agenda(inicio, fim, status);
    }
}
