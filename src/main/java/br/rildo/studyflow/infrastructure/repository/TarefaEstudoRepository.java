package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import br.rildo.studyflow.domain.TarefaEstudo;

public interface TarefaEstudoRepository {
    TarefaEstudo salvar(TarefaEstudo tarefa);
    List<TarefaEstudo> listarPorPreparacao(Long preparacaoId);
    List<TarefaEstudo> listarPorPreparacaoEData(Long preparacaoId, LocalDate data);
}