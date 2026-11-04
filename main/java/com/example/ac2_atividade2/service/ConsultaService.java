package com.example.ac2_atividade2.service;

import java.time.LocalDate;
import com.example.ac2_atividade2.dto.ConsultaRequest;
import com.example.ac2_atividade2.exception.RecursoNaoEncontradoException;
import com.example.ac2_atividade2.exception.RegraNegocioException;
import com.example.ac2_atividade2.model.Animal;
import com.example.ac2_atividade2.model.Consulta;
import com.example.ac2_atividade2.model.StatusConsulta;
import com.example.ac2_atividade2.model.Veterinario;
import com.example.ac2_atividade2.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final AnimalService animalService;
    private final VeterinarioService veterinarioService;

    public ConsultaService(ConsultaRepository consultaRepository,
                           AnimalService animalService,
                           VeterinarioService veterinarioService) {
        this.consultaRepository = consultaRepository;
        this.animalService = animalService;
        this.veterinarioService = veterinarioService;
    }

    public List<Consulta> listar(Long animalId, Long veterinarioId) {
        if (animalId != null) {
            return consultaRepository.findByAnimalId(animalId);
        }
        if (veterinarioId != null) {
            return consultaRepository.findByVeterinarioId(veterinarioId);
        }
        return consultaRepository.findAll();
    }

    public Consulta buscar(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta " + id + " não encontrada."));
    }

    @Transactional
    public Consulta agendar(ConsultaRequest r) {
        Animal animal = animalService.buscar(Validacao.obrigatorio(r.animalId(), "animalId"));
        Veterinario vet = veterinarioService.buscar(Validacao.obrigatorio(r.veterinarioId(), "veterinarioId"));
        String local = Validacao.texto(r.local(), "local");
        Validacao.obrigatorio(r.data(), "data");
        Validacao.obrigatorio(r.horario(), "horario");

        if (LocalDateTime.of(r.data(), r.horario()).isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Não é possível agendar uma consulta no passado.");
        }

        String especieAnimal = Validacao.normalizar(animal.getEspecie());
        if (!vet.getEspecies().contains(especieAnimal)) {
            throw new RegraNegocioException("O veterinário " + vet.getNome()
                    + " não está habilitado para atender a espécie '" + animal.getEspecie() + "'.");
        }

        if (consultaRepository.existsByVeterinarioIdAndDataAndHorarioAndStatusNot(
                vet.getId(), r.data(), r.horario(), StatusConsulta.CANCELADA)) {
            throw new RegraNegocioException("O veterinário já possui uma consulta neste dia e horário.");
        }

        Consulta consulta = new Consulta();
        consulta.setAnimal(animal);
        consulta.setVeterinario(vet);
        consulta.setLocal(local);
        consulta.setData(r.data());
        consulta.setHorario(r.horario());
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta cancelar(Long id) {
        Consulta consulta = buscar(id);
        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new RegraNegocioException("A consulta já está cancelada.");
        }
        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new RegraNegocioException("Não é possível cancelar uma consulta já realizada.");
        }
        consulta.setStatus(StatusConsulta.CANCELADA);
        return consultaRepository.save(consulta);
    }

    public List<Consulta> agenda(LocalDate inicio, LocalDate fim, StatusConsulta status) {
        Validacao.obrigatorio(inicio, "inicio");
        Validacao.obrigatorio(fim, "fim");
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data final não pode ser anterior à data inicial.");
        }
        return consultaRepository.agendaPorPeriodo(inicio, fim, status == null ? StatusConsulta.AGENDADA : status);
    }
}
