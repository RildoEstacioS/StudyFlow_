package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.TarefaEstudo;

@Repository
@Primary
public interface TarefaEstudoRepositoryJpa
        extends TarefaEstudoRepository, JpaRepository<TarefaEstudo, Long> {

    @Override
    default TarefaEstudo salvar(TarefaEstudo tarefa) {
        return save(tarefa);
    }

    @Override
    default List<TarefaEstudo> listarPorPreparacao(Long preparacaoId) {
        return findByPreparacaoIdOrderByData(preparacaoId);
    }

    List<TarefaEstudo> findByPreparacaoIdOrderByData(Long preparacaoId);

    @Override
    default List<TarefaEstudo> listarPorPreparacaoEData(Long preparacaoId, LocalDate data) {
        return findByPreparacaoIdAndDataOrderByData(preparacaoId, data);
    }

    List<TarefaEstudo> findByPreparacaoIdAndDataOrderByData(Long preparacaoId, LocalDate data);
}