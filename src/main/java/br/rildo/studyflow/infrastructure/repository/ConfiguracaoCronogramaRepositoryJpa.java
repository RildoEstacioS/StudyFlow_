package br.rildo.studyflow.infrastructure.repository;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.ConfiguracaoCronograma;

@Repository
@Primary
public interface ConfiguracaoCronogramaRepositoryJpa
        extends ConfiguracaoCronogramaRepository, JpaRepository<ConfiguracaoCronograma, Long> {

    @Override
    default ConfiguracaoCronograma salvar(ConfiguracaoCronograma config) {
        return save(config);
    }

    @Override
    default Optional<ConfiguracaoCronograma> buscarPorPreparacao(Long preparacaoId) {
        return findByPreparacaoId(preparacaoId);
    }

    Optional<ConfiguracaoCronograma> findByPreparacaoId(Long preparacaoId);
}