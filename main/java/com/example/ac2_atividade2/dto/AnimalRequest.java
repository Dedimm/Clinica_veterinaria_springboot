package com.example.ac2_atividade2.dto;

import java.time.LocalDate;

public record AnimalRequest(String nome, String especie, String raca, LocalDate dataNascimento, Long tutorId) {}
