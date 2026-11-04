package com.example.ac2_atividade2.dto;

import com.example.ac2_atividade2.model.Animal;
import com.example.ac2_atividade2.model.Atendimento;
import com.example.ac2_atividade2.model.Vacina;
import java.util.List;

public record HistoricoAnimal(Animal animal, List<Atendimento> atendimentos, List<Vacina> vacinas) {}
