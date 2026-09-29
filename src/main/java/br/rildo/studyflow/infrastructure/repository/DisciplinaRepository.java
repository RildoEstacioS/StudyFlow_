package br.rildo.studyflow.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import br.rildo.studyflow.domain.Disciplina;

public interface DisciplinaRepository {

    Disciplina salvar(Disciplina disciplina);

    List<Disciplina> listarPorPreparacao(Long preparacaoId);

    Optional<Disciplina> buscarPorId(Long id);

    void deletar(Long id);
}