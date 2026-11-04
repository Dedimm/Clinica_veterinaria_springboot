package com.example.ac2_atividade2.service;

import java.time.LocalDate;
import com.example.ac2_atividade2.dto.AtendimentoRequest;
import com.example.ac2_atividade2.dto.HistoricoAnimal;
import com.example.ac2_atividade2.dto.VacinaRequest;
import com.example.ac2_atividade2.exception.RecursoNaoEncontradoException;
import com.example.ac2_atividade2.exception.RegraNegocioException;
import com.example.ac2_atividade2.model.Animal;
import com.example.ac2_atividade2.model.Atendimento;
import com.example.ac2_atividade2.model.Consulta;
import com.example.ac2_atividade2.model.StatusConsulta;
import com.example.ac2_atividade2.model.Vacina;
import com.example.ac2_atividade2.repository.AtendimentoRepository;
import com.example.ac2_atividade2.repository.ConsultaRepository;
import com.example.ac2_atividade2.repository.VacinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProntuarioService {

    private final AtendimentoRepository atendimentoRepository;
    private final VacinaRepository vacinaRepository;
    private final ConsultaRepository consultaRepository;
    private final ConsultaService consultaService;
    private final AnimalService animalService;

    public ProntuarioService(AtendimentoRepository atendimentoRepository,
                             VacinaRepository vacinaRepository,
                             ConsultaRepository consultaRepository,
                             ConsultaService consultaService,
                             AnimalService animalService) {
        this.atendimentoRepository = atendimentoRepository;
        this.vacinaRepository = vacinaRepository;
        this.consultaRepository = consultaRepository;
        this.consultaService = consultaService;
        this.animalService = animalService;
    }

    @Transactional
    public Atendimento registrarAtendimento(AtendimentoRequest r) {
        Consulta consulta = consultaService.buscar(Validacao.obrigatorio(r.consultaId(), "consultaId"));
        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new RegraNegocioException("Não é possível registrar atendimento de uma consulta cancelada.");
        }
        if (atendimentoRepository.existsByConsultaId(consulta.getId())) {
            throw new RegraNegocioException("Esta consulta já possui atendimento registrado.");
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setConsulta(consulta);
        atendimento.setDiagnostico(Validacao.texto(r.diagnostico(), "diagnostico"));
        atendimento.setObservacoes(r.observacoes());
        atendimento.setOrientacoes(r.orientacoes());

        consulta.setStatus(StatusConsulta.REALIZADA);
        consultaRepository.save(consulta);
        return atendimentoRepository.save(atendimento);
    }

    public Atendimento buscarAtendimento(Long id) {
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento " + id + " não encontrado."));
    }

    public List<Atendimento> atendimentosDoAnimal(Long animalId) {
        animalService.buscar(animalId);
        return atendimentoRepository.findByConsultaAnimalIdOrderByConsultaDataDescConsultaHorarioDesc(animalId);
    }

    public Vacina registrarVacina(VacinaRequest r) {
        Vacina vacina = new Vacina();
        vacina.setAnimal(animalService.buscar(Validacao.obrigatorio(r.animalId(), "animalId")));
        vacina.setNome(Validacao.texto(r.nome(), "nome"));
        vacina.setData(Validacao.obrigatorio(r.data(), "data"));
        vacina.setLote(Validacao.texto(r.lote(), "lote"));
        return vacinaRepository.save(vacina);
    }

    public List<Vacina> vacinasDoAnimal(Long animalId) {
        animalService.buscar(animalId);
        return vacinaRepository.findByAnimalIdOrderByDataDesc(animalId);
    }

    public HistoricoAnimal historico(Long animalId) {
        Animal animal = animalService.buscar(animalId);
        return new HistoricoAnimal(animal, atendimentosDoAnimal(animalId), vacinasDoAnimal(animalId));
    }

    /** Gera uma página HTML simples, pronta para impressão (Ctrl+P). */
    public String gerarHtmlParaImpressao(Long atendimentoId) {
        Atendimento a = buscarAtendimento(atendimentoId);
        Consulta c = a.getConsulta();
        Animal animal = c.getAnimal();
        DateTimeFormatter dia = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8">
                  <title>Atendimento #%d</title>
                  <style>
                    body { font-family: Arial, sans-serif; margin: 40px; color: #222; }
                    h1 { border-bottom: 2px solid #222; padding-bottom: 8px; }
                    .campo { margin: 12px 0; }
                    .rotulo { font-weight: bold; }
                    @media print { button { display: none; } }
                  </style>
                </head>
                <body>
                  <h1>Prontuário de Atendimento #%d</h1>
                  <div class="campo"><span class="rotulo">Animal:</span> %s (%s%s)</div>
                  <div class="campo"><span class="rotulo">Tutor:</span> %s</div>
                  <div class="campo"><span class="rotulo">Veterinário:</span> %s (CRMV %s)</div>
                  <div class="campo"><span class="rotulo">Data/Hora:</span> %s às %s — %s</div>
                  <div class="campo"><span class="rotulo">Diagnóstico:</span><br>%s</div>
                  <div class="campo"><span class="rotulo">Observações:</span><br>%s</div>
                  <div class="campo"><span class="rotulo">Orientações:</span><br>%s</div>
                  <button onclick="window.print()">Imprimir</button>
                </body>
                </html>
                """.formatted(
                a.getId(), a.getId(),
                esc(animal.getNome()), esc(animal.getEspecie()),
                animal.getRaca() == null ? "" : ", " + esc(animal.getRaca()),
                esc(animal.getTutor().getNome()),
                esc(c.getVeterinario().getNome()), esc(c.getVeterinario().getCrmv()),
                c.getData().format(dia), c.getHorario(), esc(c.getLocal()),
                esc(a.getDiagnostico()), esc(a.getObservacoes()), esc(a.getOrientacoes()));
    }

    private String esc(String s) {
        if (s == null || s.isBlank()) {
            return "-";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    public List<Vacina> vacinasNoPeriodo(LocalDate inicio, LocalDate fim) {
        Validacao.obrigatorio(inicio, "inicio");
        Validacao.obrigatorio(fim, "fim");
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data final não pode ser anterior à data inicial.");
        }
        return vacinaRepository.aplicadasNoPeriodo(inicio, fim);
    }
}
