package com.example.ac2_atividade2.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String crmv;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "veterinario_especies", joinColumns = @JoinColumn(name = "veterinario_id"))
    @Column(name = "especie")
    private Set<String> especies = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrmv() { return crmv; }
    public void setCrmv(String crmv) { this.crmv = crmv; }
    public Set<String> getEspecies() { return especies; }
    public void setEspecies(Set<String> especies) { this.especies = especies; }
}
