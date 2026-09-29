package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.SessaoEstudo;

@Repository
@Primary
public interface SessaoEstudoRepositoryJpa
        extends SessaoEstudoRepository, JpaRepository<SessaoEstudo, Long> {

    @Override
    default SessaoEstudo salvar(SessaoEstudo sessao) {
        if (sessao.getDataSessao() == null) {
            sessao.setDataSessao(LocalDateTime.now());
        }
        return save(sessao);
    }

    @Override
    default List<SessaoEstudo> listarPorTopico(Long topicoId) {
        return findByTopicoIdOrderByDataSessaoDesc(topicoId);
    }

    List<SessaoEstudo> findByTopicoIdOrderByDataSessaoDesc(Long topicoId);
}