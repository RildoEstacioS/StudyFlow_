package br.rildo.studyflow.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import br.rildo.studyflow.domain.Preparacao;

public interface PreparacaoRepository {

    Preparacao salvar(Preparacao preparacao);

    List<Preparacao> listarTodos();

    Optional<Preparacao> buscarPorId(Long id);

    void deletar(Long id);
}