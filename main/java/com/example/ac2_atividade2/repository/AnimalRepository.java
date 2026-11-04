package com.example.ac2_atividade2.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.example.ac2_atividade2.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByTutorId(Long tutorId);

    @Query("SELECT a FROM Animal a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY a.nome")
    List<Animal> buscarPorNome(@Param("nome") String nome);
}
