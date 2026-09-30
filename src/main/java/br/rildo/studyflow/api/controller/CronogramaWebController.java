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

import br.rildo.studyflow.application.service.PlanejadorService;
import br.rildo.studyflow.domain.ConfigDiaSemana;
import br.rildo.studyflow.domain.ConfiguracaoCronograma;
import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.TarefaEstudo;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.domain.enums.DiaSemana;
import br.rildo.studyflow.domain.enums.FrequenciaSimulado;
import br.rildo.studyflow.domain.enums.TipoRevisao;
import br.rildo.studyflow.infrastructure.repository.ConfigDiaSemanaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.ConfiguracaoCronogramaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TarefaEstudoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class CronogramaWebController {

    private final ConfiguracaoCronogramaRepositoryJpa repositorioConfig;
    private final ConfigDiaSemanaRepositoryJpa repositorioDia;
    private final TarefaEstudoRepositoryJpa repositorioTarefa;
    private final TopicoRepositoryJpa repositorioTopico;
    private final DisciplinaRepositoryJpa disciplinaRepository;
    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final PlanejadorService planejadorService;

    public CronogramaWebController(
            ConfiguracaoCronogramaRepositoryJpa repositorioConfig,
            ConfigDiaSemanaRepositoryJpa repositorioDia,
            TarefaEstudoRepositoryJpa repositorioTarefa,
            TopicoRepositoryJpa repositorioTopico,
            DisciplinaRepositoryJpa disciplinaRepository,
            PreparacaoRepositoryJpa preparacaoRepository,
            PlanejadorService planejadorService
    ) {
        this.repositorioConfig = repositorioConfig;
        this.repositorioDia = repositorioDia;
        this.repositorioTarefa = repositorioTarefa;
        this.repositorioTopico = repositorioTopico;
        this.disciplinaRepository = disciplinaRepository;
        this.preparacaoRepository = preparacaoRepository;
        this.planejadorService = planejadorService;
    }

    // Lista todos os cronogramas
    @GetMapping("/cronogramas")
    public String listarCronogramas(Model model) {
        model.addAttribute("preparacoes", preparacaoRepository.listarTodos());
        return "cronograma-lista";
    }

    // Excluir cronograma
    @PostMapping("/cronogramas/{id}/excluir")
    public String excluirCronograma(@PathVariable Long id) {
        // Excluir tarefas, configurações e dias associados
        repositorioTarefa.excluirPorPreparacao(id);
        repositorioConfig.excluirPorPreparacao(id);
        repositorioDia.excluirPorPreparacao(id);
        preparacaoRepository.deletar(id);
        return "redirect:/cronogramas";
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

    @GetMapping("/preparacoes/{id}/cronograma/configurar")
    public String mostrarFormulario(
            @PathVariable Long id,
            Model model
    ) {
        var preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        List<Disciplina> disciplinas = disciplinaRepository.findByPreparacaoId(id);

        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(id)
                .orElse(new ConfiguracaoCronograma());

        if (config.getPreparacaoId() == null) {
            config.setPreparacaoId(id);
        }

        List<ConfigDiaSemana> configsDia = repositorioDia.listarPorPreparacao(id);
        configsDia.sort(Comparator.comparingInt(
                c -> c.getDiaSemana().getValor()
        ));

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplinas", disciplinas);
        model.addAttribute("config", config);
        model.addAttribute("configsDia", configsDia);
        model.addAttribute("frequenciasSimulado", FrequenciaSimulado.values());
        model.addAttribute("tiposRevisao", TipoRevisao.values());
        model.addAttribute("diasSemana", DiaSemana.values());

        return "cronograma-config-form";
    }

    @PostMapping("/preparacoes/{id}/cronograma/gerar")
    public String gerarCronograma(
            @PathVariable Long id,
            @RequestParam Integer materiasPorDia,
            @RequestParam String frequenciaSimulado,
            @RequestParam(required = false) String diaPreferencialSimulado,
            @RequestParam String tipoRevisao,
            @RequestParam(required = false) Integer intervaloRevisaoDias,
            @RequestParam(required = false) List<Integer> configsDia_selecionado,
            @RequestParam(required = false) List<Integer> configsDia_diaSemanaValor,
            @RequestParam(required = false) List<Integer> configsDia_minutosDiarios,
            Model model
    ) {
        var preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(id)
                .orElse(new ConfiguracaoCronograma());

        config.setPreparacaoId(id);
        config.setMateriasPorDia(materiasPorDia);
        config.setFrequenciaSimulado(FrequenciaSimulado.valueOf(frequenciaSimulado));
        config.setTipoRevisao(TipoRevisao.valueOf(tipoRevisao));
        config.setIntervaloRevisaoDias(intervaloRevisaoDias);

        if (diaPreferencialSimulado != null && !diaPreferencialSimulado.isEmpty()) {
            config.setDiaPreferencialSimulado(Integer.valueOf(diaPreferencialSimulado));
        } else {
            config.setDiaPreferencialSimulado(null);
        }

        int totalMinutos = 0;

        if (configsDia_selecionado != null && configsDia_minutosDiarios != null) {
            for (int i = 0; i < configsDia_selecionado.size(); i++) {
                int diaSelecionado = configsDia_selecionado.get(i);
                if (diaSelecionado >= 0 && diaSelecionado < configsDia_minutosDiarios.size()) {
                    totalMinutos += configsDia_minutosDiarios.get(diaSelecionado);
                }
            }
        }

        if (totalMinutos != preparacao.getMinutosDisponiveisPorSemana()) {
            List<Disciplina> disciplinas = disciplinaRepository.findByPreparacaoId(id);
            List<ConfigDiaSemana> configsDiaExistente = repositorioDia.listarPorPreparacao(id);
            configsDiaExistente.sort(Comparator.comparingInt(
                    c -> c.getDiaSemana().getValor()
            ));

            model.addAttribute("preparacao", preparacao);
            model.addAttribute("disciplinas", disciplinas);
            model.addAttribute("config", config);
            model.addAttribute("configsDia", configsDiaExistente);
            model.addAttribute("frequenciasSimulado", FrequenciaSimulado.values());
            model.addAttribute("tiposRevisao", TipoRevisao.values());
            model.addAttribute("diasSemana", DiaSemana.values());
            model.addAttribute(
                    "erroMinutos",
                    "A soma dos minutos (" + totalMinutos + ") deve ser igual aos minutos disponíveis por semana ("
                            + preparacao.getMinutosDisponiveisPorSemana() + ")."
            );

            return "cronograma-config-form";
        }

        repositorioConfig.salvar(config);
        repositorioDia.excluirPorPreparacao(id);

        if (configsDia_diaSemanaValor != null) {
            for (int i = 0; i < configsDia_diaSemanaValor.size(); i++) {
                int diaValor = configsDia_diaSemanaValor.get(i);
                if (configsDia_selecionado != null && configsDia_selecionado.contains(diaValor)) {
                    ConfigDiaSemana dia = new ConfigDiaSemana();
                    dia.setPreparacaoId(id);
                    dia.setDiaSemana(DiaSemana.deValor(diaValor));
                    dia.setMinutosDiarios(
                            configsDia_minutosDiarios != null && diaValor < configsDia_minutosDiarios.size()
                                    ? configsDia_minutosDiarios.get(diaValor)
                                    : 60
                    );
                    repositorioDia.salvar(dia);
                }
            }
        }

        planejadorService.gerarCronograma(id);

        return "redirect:/preparacoes/" + id + "/cronograma/visualizar?hoje=true";
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
        int anoAtual = (hoje != null && hoje) ? hojeDate.getYear()
                : (ano != null && mes != null) ? ano
                        : hojeDate.getYear();
        int mesAtual = (hoje != null && hoje) ? hojeDate.getMonthValue()
                : (ano != null && mes != null) ? mes
                        : hojeDate.getMonthValue();

        LocalDate primeiroDia = LocalDate.of(anoAtual, mesAtual, 1);
        LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());

        List<TarefaEstudo> tarefasMes = repositorioTarefa.listarPorPreparacao(id)
                .stream()
                .filter(t -> !t.getData().isBefore(primeiroDia) && !t.getData().isAfter(ultimoDia))
                .sorted(Comparator.comparing(TarefaEstudo::getData))
                .collect(Collectors.toList());

        Map<LocalDate, List<TarefaEstudo>> tarefasPorDia = new HashMap<>();
        for (TarefaEstudo tarefa : tarefasMes) {
            tarefasPorDia.computeIfAbsent(tarefa.getData(), k -> new ArrayList<>()).add(tarefa);
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
}