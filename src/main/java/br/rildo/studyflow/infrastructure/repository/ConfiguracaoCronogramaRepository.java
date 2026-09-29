package br.rildo.studyflow.infrastructure.repository;

import java.util.Optional;

import br.rildo.studyflow.domain.ConfiguracaoCronograma;

public interface ConfiguracaoCronogramaRepository {
    ConfiguracaoCronograma salvar(ConfiguracaoCronograma config);
    Optional<ConfiguracaoCronograma> buscarPorPreparacao(Long preparacaoId);
}