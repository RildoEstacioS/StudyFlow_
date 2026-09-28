package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import br.rildo.studyflow.domain.Revisao;
import br.rildo.studyflow.domain.enums.StatusRevisao;

public class RevisaoRepositoryEmMemoria implements RevisaoRepository {
    private final Map<Long, Revisao> revisoes = new ConcurrentHashMap<>();
    private long proximoId = 1;

    @Override
    public List<Revisao> listarPorTopico(Long topicoId) {
        return revisoes.values().stream()
            .filter(r -> r.getTopicoId().equals(topicoId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Revisao> listarPendentes() {
        return revisoes.values().stream()
            .filter(r -> r.getStatus() == StatusRevisao.PENDENTE)
            .collect(Collectors.toList());
    }

    @Override
    public List<Revisao> listarVencidas(LocalDate dataReferencia) {
        return revisoes.values().stream()
            .filter(r -> r.getStatus() == StatusRevisao.PENDENTE)
            .filter(r -> r.getDataPrevista().isBefore(dataReferencia))
            .collect(Collectors.toList());
    }

    @Override
    public void criarRevisao(Long topicoId, int numeroRevisao, LocalDate dataPrevista) {
        Revisao revisao = new Revisao();
        revisao.setId(proximoId++);
        revisao.setTopicoId(topicoId);
        revisao.setNumeroRevisao(numeroRevisao);
        revisao.setDataPrevista(dataPrevista);
        revisao.setStatus(StatusRevisao.PENDENTE);
        revisao.setCriadaEm(LocalDateTime.now());

        revisoes.put(revisao.getId(), revisao);
    }

    @Override
    public void marcarComoRealizada(Long id, LocalDate dataRealizada) {
        Revisao revisao = revisoes.get(id);
        if (revisao != null) {
            revisao.setDataRealizada(dataRealizada);
            revisao.setStatus(StatusRevisao.CONCLUIDA);
        }
    }

    @Override
    public Optional<Revisao> buscarPorId(Long id) {
        return Optional.ofNullable(revisoes.get(id));
    }
    @Override
    public List<Revisao> listarRevisoesDoDia(LocalDate data) {
  
        return revisoes.values().stream()
        .filter(r -> r.getDataPrevista().isEqual(data))
        .collect(Collectors.toList());
    }
    @Override
    public List<Revisao> listarTodas() {
        return new ArrayList<>(revisoes.values());
    }
}