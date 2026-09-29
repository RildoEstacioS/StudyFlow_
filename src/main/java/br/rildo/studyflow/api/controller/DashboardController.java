package br.rildo.studyflow.api.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.rildo.studyflow.domain.DesempenhoTopico;
import br.rildo.studyflow.domain.Revisao;
import br.rildo.studyflow.domain.enums.StatusRevisao;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class DashboardController {

    private final TopicoRepositoryJpa topicoRepository;
    private final DesempenhoRepositoryJpa desempenhoRepository;
    private final RevisaoRepositoryJpa revisaoRepository;

    public DashboardController(
            TopicoRepositoryJpa topicoRepository,
            DesempenhoRepositoryJpa desempenhoRepository,
            RevisaoRepositoryJpa revisaoRepository
    ) {
        this.topicoRepository = topicoRepository;
        this.desempenhoRepository = desempenhoRepository;
        this.revisaoRepository = revisaoRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<?> todosTopicos = topicoRepository.findAll();
        List<?> todasRevisoes = revisaoRepository.findAll();
        List<DesempenhoTopico> todosDesempenhos = desempenhoRepository.findAll();

        long totalTopicos = todosTopicos == null ? 0 : todosTopicos.size();
        long totalRevisoes = todasRevisoes == null ? 0 : todasRevisoes.size();

        long revisoesPendentes = todasRevisoes == null
                ? 0
                : todasRevisoes.stream()
                    .filter(r -> r instanceof Revisao)
                    .map(r -> (Revisao) r)
                    .filter(r -> r.getStatus() == StatusRevisao.PENDENTE)
                    .count();

        double mediaAcertos = todosDesempenhos == null || todosDesempenhos.isEmpty()
                ? 0.0
                : todosDesempenhos.stream()
                    .mapToInt(DesempenhoTopico::getPercentualAcertos)
                    .average()
                    .orElse(0.0);

        model.addAttribute("totalTopicos", totalTopicos);
        model.addAttribute("totalRevisoes", totalRevisoes);
        model.addAttribute("revisoesPendentes", revisoesPendentes);
        model.addAttribute("mediaAcertos", Math.round(mediaAcertos));

        return "dashboard";
    }
    @GetMapping("/teste")
    @ResponseBody
    public String teste() {
        return "DashboardController funcionando";
    }
}