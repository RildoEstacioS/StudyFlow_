package br.rildo.studyflow.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.TarefaEstudo;

@Repository
public interface TarefaEstudoRepositoryJpa extends JpaRepository<TarefaEstudo, Long> {

    List<TarefaEstudo> listarPorPreparacao(Long preparacaoId);

    @Modifying
    @Query("DELETE FROM TarefaEstudo t WHERE t.preparacaoId = :preparacaoId")
    void excluirPorPreparacao(@Param("preparacaoId") Long preparacaoId);
}