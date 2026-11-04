package com.example.ac2_atividade2.repository;

import com.example.ac2_atividade2.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    boolean existsByConsultaId(Long consultaId);

    List<Atendimento> findByConsultaAnimalIdOrderByConsultaDataDescConsultaHorarioDesc(Long animalId);
}
