package br.rildo.studyflow.application.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.rildo.studyflow.domain.ConfigDiaSemana;
import br.rildo.studyflow.domain.ConfiguracaoCronograma;
import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.TarefaEstudo;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.domain.enums.FrequenciaSimulado;
import br.rildo.studyflow.domain.enums.TipoTarefa;
import br.rildo.studyflow.infrastructure.repository.ConfigDiaSemanaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.ConfiguracaoCronogramaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.SessaoEstudoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TarefaEstudoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Service
public class PlanejadorService {

    private final ConfiguracaoCronogramaRepositoryJpa repositorioConfig;
    private final ConfigDiaSemanaRepositoryJpa repositorioDia;
    private final TopicoRepositoryJpa repositorioTopico;
    private final DisciplinaRepositoryJpa repositorioDisciplina;
    private final TarefaEstudoRepositoryJpa repositorioTarefa;
    private final RevisaoRepositoryJpa repositorioRevisao;
    private final SessaoEstudoRepositoryJpa repositorioSessao;

    public PlanejadorService(
            ConfiguracaoCronogramaRepositoryJpa repositorioConfig,
            ConfigDiaSemanaRepositoryJpa repositorioDia,
            TopicoRepositoryJpa repositorioTopico,
            DisciplinaRepositoryJpa repositorioDisciplina,
            TarefaEstudoRepositoryJpa repositorioTarefa,
            RevisaoRepositoryJpa repositorioRevisao,
            SessaoEstudoRepositoryJpa repositorioSessao
    ) {
        this.repositorioConfig = repositorioConfig;
        this.repositorioDia = repositorioDia;
        this.repositorioTopico = repositorioTopico;
        this.repositorioDisciplina = repositorioDisciplina;
        this.repositorioTarefa = repositorioTarefa;
        this.repositorioRevisao = repositorioRevisao;
        this.repositorioSessao = repositorioSessao;
    }

    @Transactional
    public void gerarCronograma(Long preparacaoId) {
        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(preparacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Configuração de cronograma não encontrada para a preparação "
                                + preparacaoId
                ));

        List<ConfigDiaSemana> configsDia = repositorioDia
                .listarPorPreparacao(preparacaoId);

        configsDia.sort(Comparator.comparingInt(
                c -> c.getDiaSemana().getValor()
        ));

        if (configsDia.isEmpty()) {
            throw new IllegalArgumentException(
                    "Configure pelo menos um dia de estudo antes de gerar o cronograma."
            );
        }

        List<Disciplina> disciplinas = repositorioDisciplina
                .findByPreparacaoId(preparacaoId);

        if (disciplinas == null || disciplinas.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cadastre pelo menos uma disciplina antes de gerar o cronograma."
            );
        }

        List<Topico> topicos = repositorioTopico
                .findByPreparacaoId(preparacaoId);

        if (topicos == null || topicos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cadastre pelo menos um tópico antes de gerar o cronograma."
            );
        }

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(90);

        List<TarefaEstudo> tarefasAntigas = repositorioTarefa
                .listarPorPreparacao(preparacaoId);

        if (tarefasAntigas != null && !tarefasAntigas.isEmpty()) {
            repositorioTarefa.deleteAll(tarefasAntigas);
        }

        List<TarefaEstudo> novasTarefas = new ArrayList<>();

        novasTarefas.addAll(distribuirEstudoERevisao(
                preparacaoId,
                config,
                configsDia,
                disciplinas,
                topicos,
                hoje,
                limite
        ));

        novasTarefas.addAll(inserirSimulados(
                preparacaoId,
                config,
                configsDia,
                hoje,
                limite
        ));

        for (TarefaEstudo tarefa : novasTarefas) {
            repositorioTarefa.salvar(tarefa);
        }

        if (novasTarefas.isEmpty()) {
            throw new IllegalArgumentException(
                    "Não foi possível gerar tarefas. Verifique os minutos configurados nos dias de estudo."
            );
        }
    }

    private List<TarefaEstudo> distribuirEstudoERevisao(
            Long preparacaoId,
            ConfiguracaoCronograma config,
            List<ConfigDiaSemana> configsDia,
            List<Disciplina> disciplinas,
            List<Topico> topicos,
            LocalDate hoje,
            LocalDate limite
    ) {
        List<TarefaEstudo> tarefas = new ArrayList<>();

        // Agrupa tópicos por disciplina
        Map<Long, List<Topico>> topicosPorDisciplina = new HashMap<>();
        for (Topico topico : topicos) {
            Long disciplinaId = topico.getDisciplinaId();
            if (disciplinaId == null) {
                continue;
            }
            topicosPorDisciplina
                    .computeIfAbsent(disciplinaId, k -> new ArrayList<>())
                    .add(topico);
        }

        // Calcula peso total
        int pesoTotal = disciplinas.stream()
                .mapToInt(Disciplina::getPeso)
                .sum();

        // Calcula minutos semanais totais
        int minutosSemanaTotal = configsDia.stream()
                .mapToInt(ConfigDiaSemana::getMinutosDiarios)
                .sum();

        // Calcula minutos por disciplina baseado no peso
        Map<Long, Integer> minutosPorDisciplina = new HashMap<>();
        for (Disciplina disciplina : disciplinas) {
            int minutos = (int) Math.round(
                    (double) disciplina.getPeso() / pesoTotal * minutosSemanaTotal
            );
            minutosPorDisciplina.put(disciplina.getId(), minutos);
        }

        // Ordena tópicos por prioridade: NAO_INICIADO > EM_ESTUDO > EM_REVISAO > DOMINADO
        for (List<Topico> lista : topicosPorDisciplina.values()) {
            lista.sort(Comparator.comparingInt(t -> {
                switch (t.getStatus()) {
                    case NAO_INICIADO: return 0;
                    case EM_ESTUDO: return 1;
                    case EM_REVISAO: return 2;
                    case DOMINADO: return 3;
                    default: return 4;
                }
            }));
        }

        // Mapa de índice por disciplina para controle de progresso
        Map<Long, Integer> indicePorDisciplina = new HashMap<>();
        for (Long disciplinaId : topicosPorDisciplina.keySet()) {
            indicePorDisciplina.put(disciplinaId, 0);
        }

        // Controle de última disciplina para evitar repetição consecutiva
        Long ultimaDisciplina = null;

        LocalDate data = hoje;

        while (!data.isAfter(limite)) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            int valorDiaJava = diaSemana.getValue();
            int valorDiaEnum = (valorDiaJava == 7) ? 0 : valorDiaJava;

            ConfigDiaSemana configDia = configsDia.stream()
                    .filter(c -> c.getDiaSemana().getValor() == valorDiaEnum)
                    .findFirst()
                    .orElse(null);

            if (configDia == null || configDia.getMinutosDiarios() <= 0) {
                data = data.plusDays(1);
                continue;
            }

            int materiasNoDia = config.getMateriasPorDia();
            int minutosDisponiveis = configDia.getMinutosDiarios();
            int minutosPorMateria = 30;
            int maxMaterias = Math.min(materiasNoDia, minutosDisponiveis / minutosPorMateria);

            // Seleciona disciplinas para o dia (rodízio)
            List<Disciplina> disciplinasDoDia = selecionarDisciplinasDoDia(
                    disciplinas,
                    ultimaDisciplina,
                    topicosPorDisciplina,
                    indicePorDisciplina,
                    minutosPorDisciplina,
                    maxMaterias
            );

            for (Disciplina disciplina : disciplinasDoDia) {
                Long disciplinaId = disciplina.getId();
                List<Topico> topicosDaDisciplina = topicosPorDisciplina.get(disciplinaId);
                int indice = indicePorDisciplina.getOrDefault(disciplinaId, 0);

                if (indice < topicosDaDisciplina.size()) {
                    Topico topico = topicosDaDisciplina.get(indice);
                    indicePorDisciplina.put(disciplinaId, indice + 1);

                    TarefaEstudo tarefa = new TarefaEstudo();
                    tarefa.setPreparacaoId(preparacaoId);
                    tarefa.setTopicoId(topico.getId());
                    tarefa.setData(data);
                    tarefa.setTipo(TipoTarefa.ESTUDO_NOVO);
                    tarefa.setConcluida(false);

                    tarefas.add(tarefa);
                }
            }

            if (!disciplinasDoDia.isEmpty()) {
                ultimaDisciplina = disciplinasDoDia.get(0).getId();
            }

            data = data.plusDays(1);
        }

        return tarefas;
    }

    private List<Disciplina> selecionarDisciplinasDoDia(
            List<Disciplina> todasDisciplinas,
            Long ultimaDisciplina,
            Map<Long, List<Topico>> topicosPorDisciplina,
            Map<Long, Integer> indicePorDisciplina,
            Map<Long, Integer> minutosPorDisciplina,
            int maxMaterias
    ) {
        // Filtra disciplinas que ainda têm tópicos não alocados
        List<Disciplina> elegiveis = todasDisciplinas.stream()
                .filter(d -> {
                    List<Topico> topicos = topicosPorDisciplina.get(d.getId());
                    int indice = indicePorDisciplina.getOrDefault(d.getId(), 0);
                    return topicos != null && indice < topicos.size();
                })
                .collect(Collectors.toList());

        if (elegiveis.isEmpty()) {
            return new ArrayList<>();
        }

        // Se possível, evita repetir a última disciplina
        if (ultimaDisciplina != null && elegiveis.size() > 1) {
            elegiveis.removeIf(d -> d.getId().equals(ultimaDisciplina));
        }

        // Ordena por: minutos restantes (prioriza quem tem mais minutos para cumprir)
        elegiveis.sort((d1, d2) -> {
            int minutos1 = minutosPorDisciplina.getOrDefault(d1.getId(), 0);
            int minutos2 = minutosPorDisciplina.getOrDefault(d2.getId(), 0);
            return Integer.compare(minutos2, minutos1); // Descendente
        });

        // Retorna até maxMaterias
        return elegiveis.stream()
                .limit(maxMaterias)
                .collect(Collectors.toList());
    }

    private List<TarefaEstudo> inserirSimulados(
            Long preparacaoId,
            ConfiguracaoCronograma config,
            List<ConfigDiaSemana> configsDia,
            LocalDate hoje,
            LocalDate limite
    ) {
        List<TarefaEstudo> simulados = new ArrayList<>();

        if (config.getFrequenciaSimulado() == FrequenciaSimulado.NENHUM) {
            return simulados;
        }

        int periodoDias;

        switch (config.getFrequenciaSimulado()) {
            case SEMANAL:
                periodoDias = 7;
                break;
            case QUIZENAL:
                periodoDias = 14;
                break;
            case MENSAL:
                periodoDias = 30;
                break;
            default:
                return simulados;
        }

        Integer diaPref = config.getDiaPreferencialSimulado();
        int diaSemanaSimulado = diaPref != null ? diaPref : 0;

        LocalDate data = hoje;
        int contador = 0;

        while (!data.isAfter(limite)) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            int valorDiaJava = diaSemana.getValue();
            int valorDiaEnum = (valorDiaJava == 7) ? 0 : valorDiaJava;

            if (valorDiaEnum == diaSemanaSimulado && contador % periodoDias == 0) {
                TarefaEstudo simulado = new TarefaEstudo();
                simulado.setPreparacaoId(preparacaoId);
                simulado.setTopicoId(null);
                simulado.setData(data);
                simulado.setTipo(TipoTarefa.SIMULADO);
                simulado.setConcluida(false);

                simulados.add(simulado);
            }

            contador++;
            data = data.plusDays(1);
        }

        return simulados;
    }
}