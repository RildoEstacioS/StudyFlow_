package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.Disciplina;

@Repository
@Primary
public interface DisciplinaRepositoryJpa
        extends DisciplinaRepository, JpaRepository<Disciplina, Long> {

    @Override
    default Disciplina salvar(Disciplina disciplina) {
        if (disciplina.getCriadaEm() == null) {
            disciplina.setCriadaEm(LocalDateTime.now());
        }

        return save(disciplina);
    }

    @Override
    default List<Disciplina> listarPorPreparacao(Long preparacaoId) {
        return findByPreparacaoId(preparacaoId);
    }

    @Override
    default Optional<Disciplina> buscarPorId(Long id) {
        return findById(id);
    }

    @Override
    default void deletar(Long id) {
        deleteById(id);
    }

    List<Disciplina> findByPreparacaoId(Long preparacaoId);
}