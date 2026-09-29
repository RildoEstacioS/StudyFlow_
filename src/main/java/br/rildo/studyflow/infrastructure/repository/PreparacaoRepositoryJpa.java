package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.Preparacao;

@Repository
@Primary
public interface PreparacaoRepositoryJpa
        extends PreparacaoRepository, JpaRepository<Preparacao, Long> {

    @Override
    default Preparacao salvar(Preparacao preparacao) {
        if (preparacao.getCriadaEm() == null) {
            preparacao.setCriadaEm(LocalDateTime.now());
        }

        return save(preparacao);
    }

    @Override
    default List<Preparacao> listarTodos() {
        return findAll();
    }

    @Override
    default Optional<Preparacao> buscarPorId(Long id) {
        return findById(id);
    }

    @Override
    default void deletar(Long id) {
        deleteById(id);
    }
}