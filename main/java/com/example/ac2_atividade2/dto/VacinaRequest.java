package com.example.ac2_atividade2.dto;

import java.time.LocalDate;

public record VacinaRequest(Long animalId, String nome, LocalDate data, String lote) {}
