package br.rildo.studyflow.infrastructure.repository;

import java.util.List;

import br.rildo.studyflow.domain.Topico;

public interface TopicoRepository {

    Topico salvar(Topico topico);

    List<Topico> listarPorPreparacao(Long preparacaoId);

    List<Topico> listarPorDisciplina(Long disciplinaId);
}