package br.rildo.studyflow.infrastructure.repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.ConfigDiaSemana;

@Repository
@Primary
public interface ConfigDiaSemanaRepositoryJpa
        extends ConfigDiaSemanaRepository, JpaRepository<ConfigDiaSemana, Long> {

    @Override
    default ConfigDiaSemana salvar(ConfigDiaSemana config) {
        return save(config);
    }

    @Override
    default List<ConfigDiaSemana> listarPorPreparacao(Long preparacaoId) {
        return findByPreparacaoId(preparacaoId);
    }

    List<ConfigDiaSemana> findByPreparacaoId(Long preparacaoId);

    @Override
    default void excluirPorPreparacao(Long preparacaoId) {
        deleteAll(findByPreparacaoId(preparacaoId));
    }
}