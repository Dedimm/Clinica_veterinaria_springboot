package com.example.ac2_atividade2.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.example.ac2_atividade2.model.Consulta;
import com.example.ac2_atividade2.model.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByVeterinarioIdAndDataAndHorarioAndStatusNot(
            Long veterinarioId, LocalDate data, LocalTime horario, StatusConsulta status);

    List<Consulta> findByAnimalId(Long animalId);
    List<Consulta> findByVeterinarioId(Long veterinarioId);

    @Query("SELECT c FROM Consulta c WHERE c.data BETWEEN :inicio AND :fim AND c.status = :status "
            + "ORDER BY c.data, c.horario")
    List<Consulta> agendaPorPeriodo(@Param("inicio") LocalDate inicio,
                                    @Param("fim") LocalDate fim,
                                    @Param("status") StatusConsulta status);
}
