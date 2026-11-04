package com.example.ac2_atividade2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaRequest(Long animalId, Long veterinarioId, String local, LocalDate data, LocalTime horario) {}
