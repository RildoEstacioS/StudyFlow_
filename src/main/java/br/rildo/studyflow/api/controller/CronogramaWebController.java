package br.rildo.studyflow.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.rildo.studyflow.domain.ConfiguracaoCronograma;
import br.rildo.studyflow.domain.TarefaEstudo;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.infrastructure.repository.ConfiguracaoCronogramaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TarefaEstudoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class CronogramaWebController {

    private final ConfiguracaoCronogramaRepositoryJpa repositorioConfig;
    private final TarefaEstudoRepositoryJpa repositorioTarefa;
    private final TopicoRepositoryJpa repositorioTopico;

    public CronogramaWebController(
            ConfiguracaoCronogramaRepositoryJpa repositorioConfig,
            TarefaEstudoRepositoryJpa repositorioTarefa,
            TopicoRepositoryJpa repositorioTopico
    ) {
        this.repositorioConfig = repositorioConfig;
        this.repositorioTarefa = repositorioTarefa;
        this.repositorioTopico = repositorioTopico;
    }

    @GetMapping("/cronograma")
    public String cronogramaGeral(
            @RequestParam(required = false) Long preparacaoId,
            Model model
    ) {
        if (preparacaoId == null) {
            return "redirect:/preparacoes";
        }

        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(preparacaoId)
                .orElse(null);

        if (config == null) {
            return "redirect:/preparacoes/" + preparacaoId + "/cronograma/configurar";
        }

        return "redirect:/preparacoes/" + preparacaoId + "/cronograma/visualizar";
    }

    @GetMapping("/preparacoes/{id}/cronograma/visualizar")
    public String visualizarCronograma(
            @PathVariable Long id,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Boolean hoje,
            Model model
    ) {
        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(id)
                .orElse(null);

        if (config == null) {
            return "redirect:/preparacoes/" + id + "/cronograma/configurar";
        }

        LocalDate hojeDate = LocalDate.now();

        int anoAtual;
        int mesAtual;

        if (hoje != null && hoje) {
            anoAtual = hojeDate.getYear();
            mesAtual = hojeDate.getMonthValue();
        } else if (ano != null && mes != null) {
            anoAtual = ano;
            mesAtual = mes;
        } else {
            anoAtual = hojeDate.getYear();
            mesAtual = hojeDate.getMonthValue();
        }

        LocalDate primeiroDia = LocalDate.of(anoAtual, mesAtual, 1);
        LocalDate ultimoDia = primeiroDia.withDayOfMonth(
                primeiroDia.lengthOfMonth()
        );

        List<TarefaEstudo> tarefasMes = repositorioTarefa
                .listarPorPreparacao(id)
                .stream()
                .filter(t -> !t.getData().isBefore(primeiroDia)
                        && !t.getData().isAfter(ultimoDia))
                .sorted(Comparator.comparing(TarefaEstudo::getData))
                .collect(Collectors.toList());

        Map<LocalDate, List<TarefaEstudo>> tarefasPorDia = new HashMap<>();
        for (TarefaEstudo tarefa : tarefasMes) {
            tarefasPorDia
                    .computeIfAbsent(tarefa.getData(), k -> new ArrayList<>())
                    .add(tarefa);
        }

        List<Topico> topicos = repositorioTopico.findByPreparacaoId(id);
        Map<Long, Topico> topicoPorId = new HashMap<>();
        for (Topico t : topicos) {
            topicoPorId.put(t.getId(), t);
        }

        String[] nomesMeses = {
            "", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };

        model.addAttribute("preparacaoId", id);
        model.addAttribute("ano", anoAtual);
        model.addAttribute("mes", mesAtual);
        model.addAttribute("nomeMes", nomesMeses[mesAtual]);
        model.addAttribute("tarefasPorDia", tarefasPorDia);
        model.addAttribute("topicoPorId", topicoPorId);
        model.addAttribute("hoje", (hoje != null && hoje));

        return "cronograma-visualizar";
    }

    @GetMapping("/preparacoes/{id}/cronograma/hoje")
    public String irParaHoje(@PathVariable Long id) {
        return "redirect:/preparacoes/" + id + "/cronograma/visualizar?hoje=true";
    }

    @PostMapping("/preparacoes/{id}/cronograma/gerar")
    public String gerarCronograma(@PathVariable Long id) {
        // Aqui entraria a chamada ao PlanejadorService
        return "redirect:/preparacoes/" + id + "/cronograma/visualizar";
    }
}