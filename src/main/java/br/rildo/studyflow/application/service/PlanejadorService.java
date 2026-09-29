package br.rildo.studyflow.application.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public void gerarCronograma(Long preparacaoId) {
        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(preparacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Configuração de cronograma não encontrada para a preparação " + preparacaoId
                ));

        List<ConfigDiaSemana> configsDia = repositorioDia.listarPorPreparacao(preparacaoId);
        configsDia.sort(Comparator.comparingInt(c -> c.getDiaSemana().getValor()));

        List<Disciplina> disciplinas =
                repositorioDisciplina.findByPreparacaoId(preparacaoId);

        List<Topico> topicos =
                repositorioTopico.findByPreparacaoId(preparacaoId);

        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(90);

        List<TarefaEstudo> tarefasAntigas = repositorioTarefa.listarPorPreparacao(preparacaoId);
        repositorioTarefa.deleteAll(tarefasAntigas);

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

        topicos.sort(Comparator.comparingInt(t -> {
            switch (t.getStatus()) {
                case NAO_INICIADO: return 0;
                case EM_ESTUDO: return 1;
                case EM_REVISAO: return 2;
                case DOMINADO: return 3;
                default: return 4;
            }
        }));

        LocalDate data = hoje;
        int indiceTopico = 0;

        while (!data.isAfter(limite)) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            int valorDiaJava = diaSemana.getValue();

            int valorDiaEnum;
            if (valorDiaJava == 7) {
                valorDiaEnum = 0;
            } else {
                valorDiaEnum = valorDiaJava;
            }

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
            int maxMaterias = minutosDisponiveis / minutosPorMateria;
            int materiasParaAlocar = Math.min(materiasNoDia, maxMaterias);

            for (int i = 0; i < materiasParaAlocar && indiceTopico < topicos.size(); i++) {
                Topico topico = topicos.get(indiceTopico++);

                TarefaEstudo tarefa = new TarefaEstudo();
                tarefa.setPreparacaoId(preparacaoId);
                tarefa.setTopicoId(topico.getId());
                tarefa.setData(data);
                tarefa.setTipo(TipoTarefa.ESTUDO_NOVO);
                tarefa.setConcluida(false);

                tarefas.add(tarefa);
            }

            data = data.plusDays(1);
        }

        return tarefas;
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
            case SEMANAL: periodoDias = 7; break;
            case QUIZENAL: periodoDias = 14; break;
            case MENSAL: periodoDias = 30; break;
            default: return simulados;
        }

        Integer diaPref = config.getDiaPreferencialSimulado();
        int diaSemanaSimulado = (diaPref != null) ? diaPref : 0;

        LocalDate data = hoje;
        int contador = 0;

        while (!data.isAfter(limite)) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            int valorDiaJava = diaSemana.getValue();

            int valorDiaEnum;
            if (valorDiaJava == 7) {
                valorDiaEnum = 0;
            } else {
                valorDiaEnum = valorDiaJava;
            }

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