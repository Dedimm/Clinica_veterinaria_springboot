package com.example.ac2_atividade2.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.example.ac2_atividade2.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);

    @Query("SELECT t FROM Tutor t WHERE (SELECT COUNT(a) FROM Animal a WHERE a.tutor.id = t.id) >= :minimo "
            + "ORDER BY t.nome")
    List<Tutor> comPeloMenosAnimais(@Param("minimo") long minimo);
}
