package com.example.ac2_atividade2.repository;

import java.time.LocalDate;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.example.ac2_atividade2.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VacinaRepository extends JpaRepository<Vacina, Long> {
    List<Vacina> findByAnimalIdOrderByDataDesc(Long animalId);

    @Query("SELECT v FROM Vacina v WHERE v.data BETWEEN :inicio AND :fim ORDER BY v.data DESC")
    List<Vacina> aplicadasNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
