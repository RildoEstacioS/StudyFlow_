package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.Revisao;
import br.rildo.studyflow.domain.enums.StatusRevisao;

@Repository
@Primary
public interface RevisaoRepositoryJpa
        extends RevisaoRepository, JpaRepository<Revisao, Long> {

    @Override
    default List<Revisao> listarPorTopico(Long topicoId) {
        return findByTopicoId(topicoId);
    }

    @Override
    default List<Revisao> listarPendentes() {
        return findByStatus(StatusRevisao.PENDENTE);
    }

    @Override
    default List<Revisao> listarVencidas(LocalDate dataReferencia) {
        return findByDataPrevistaBeforeAndStatus(
                dataReferencia,
                StatusRevisao.PENDENTE
        );
    }

    @Override
    default void criarRevisao(
            Long topicoId,
            int numeroRevisao,
            LocalDate dataPrevista
    ) {
        Revisao revisao = new Revisao();
        revisao.setTopicoId(topicoId);
        revisao.setTopicoNome("Tópico " + topicoId);
        revisao.setNumeroRevisao(numeroRevisao);
        revisao.setDataPrevista(dataPrevista);
        revisao.setStatus(StatusRevisao.PENDENTE);
        revisao.setCriadaEm(LocalDateTime.now());

        save(revisao);
    }

    @Override
    default void marcarComoRealizada(Long id, LocalDate dataRealizada) {
        findById(id).ifPresent(revisao -> {
            revisao.setDataRealizada(dataRealizada);
            revisao.setStatus(StatusRevisao.CONCLUIDA);
            save(revisao);
        });
    }

    @Override
    default Optional<Revisao> buscarPorId(Long id) {
        return findById(id);
    }

    @Override
    default List<Revisao> listarRevisoesDoDia(LocalDate data) {
        return findByDataPrevista(data);
    }

    @Override
    default List<Revisao> listarTodas() {
        return findAll();
    }

    List<Revisao> findByTopicoId(Long topicoId);

    List<Revisao> findByStatus(StatusRevisao status);

    List<Revisao> findByDataPrevista(LocalDate dataPrevista);

    List<Revisao> findByDataPrevistaBeforeAndStatus(
            LocalDate dataPrevista,
            StatusRevisao status
    );
}