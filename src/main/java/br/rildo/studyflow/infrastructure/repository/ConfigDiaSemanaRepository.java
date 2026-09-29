package br.rildo.studyflow.infrastructure.repository;

import java.util.List;

import br.rildo.studyflow.domain.ConfigDiaSemana;

public interface ConfigDiaSemanaRepository {
    ConfigDiaSemana salvar(ConfigDiaSemana config);
    List<ConfigDiaSemana> listarPorPreparacao(Long preparacaoId);
    void excluirPorPreparacao(Long preparacaoId);
}