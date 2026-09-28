package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import br.rildo.studyflow.domain.Revisao;

public interface RevisaoRepository {
    List<Revisao> listarPorTopico(Long topicoId);
    List<Revisao> listarPendentes();
    List<Revisao> listarVencidas(LocalDate dataReferencia);
    void criarRevisao(Long topicoId, int numeroRevisao, LocalDate dataPrevista);
    void marcarComoRealizada(Long id, LocalDate dataRealizada);
    Optional<Revisao> buscarPorId(Long id);
    List<Revisao> listarRevisoesDoDia(LocalDate data);
    List<Revisao> listarTodas();
}