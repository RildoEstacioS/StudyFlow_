package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.Topico;

@Repository
@Primary
public interface TopicoRepositoryJpa
        extends TopicoRepository, JpaRepository<Topico, Long> {

    @Override
    default Topico salvar(Topico topico) {
        if (topico.getCriadoEm() == null) {
            topico.setCriadoEm(LocalDateTime.now());
        }

        return save(topico);
    }

    @Override
    default List<Topico> listarPorPreparacao(Long preparacaoId) {
        return findByPreparacaoId(preparacaoId);
    }

    @Override
    default List<Topico> listarPorDisciplina(Long disciplinaId) {
        return findByDisciplinaId(disciplinaId);
    }

    List<Topico> findByPreparacaoId(Long preparacaoId);

    List<Topico> findByDisciplinaId(Long disciplinaId);
}