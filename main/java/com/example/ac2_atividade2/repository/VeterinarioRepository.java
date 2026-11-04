package com.example.ac2_atividade2.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.example.ac2_atividade2.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    boolean existsByCrmv(String crmv);
    boolean existsByCrmvAndIdNot(String crmv, Long id);

    @Query("SELECT DISTINCT v FROM Veterinario v JOIN v.especies e WHERE e = :especie ORDER BY v.nome")
    List<Veterinario> buscarPorEspecie(@Param("especie") String especie);
}
