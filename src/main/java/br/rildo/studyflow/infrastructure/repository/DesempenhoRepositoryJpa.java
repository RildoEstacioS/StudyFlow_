package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.DesempenhoTopico;

@Repository
@Primary
public interface DesempenhoRepositoryJpa
        extends DesempenhoRepository, JpaRepository<DesempenhoTopico, Long> {

    @Override
    default DesempenhoTopico salvar(DesempenhoTopico desempenho) {
        if (desempenho.getUltimaAtualizacao() == null) {
            desempenho.setUltimaAtualizacao(LocalDateTime.now());
        }

        return save(desempenho);
    }

    @Override
    default Optional<DesempenhoTopico> buscarPorTopico(Long topicoId) {
        return findByTopicoId(topicoId);
    }

    Optional<DesempenhoTopico> findFirstByTopicoIdOrderByUltimaAtualizacaoDesc(Long topicoId);

    Optional<DesempenhoTopico> findByTopicoId(Long topicoId);
}