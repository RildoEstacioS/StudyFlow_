package br.rildo.studyflow.application.service;



import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.rildo.studyflow.api.dto.DashboardResponse;
import br.rildo.studyflow.api.dto.RevisaoResponse;
import br.rildo.studyflow.domain.Revisao;
import br.rildo.studyflow.domain.enums.StatusRevisao;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepository;

public class RevisaoService {
    private final RevisaoRepository repositorio;

    public RevisaoService(RevisaoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<RevisaoResponse> listarRevisoesPorTopico(Long topicoId) {
        return repositorio.listarPorTopico(topicoId).stream()
            .map(r -> new RevisaoResponse(
                r.getId(),
                r.getTopicoId(),
                r.getNumeroRevisao(),
                r.getDataPrevista(),
                r.getDataRealizada(),
                r.getStatus(),
                r.getCriadaEm()
            ))
            .collect(Collectors.toList());
    }

    public List<RevisaoResponse> listarRevisoesPendentes() {
        return repositorio.listarPendentes().stream()
            .map(r -> new RevisaoResponse(
                r.getId(),
                r.getTopicoId(),
                r.getNumeroRevisao(),
                r.getDataPrevista(),
                r.getDataRealizada(),
                r.getStatus(),
                r.getCriadaEm()
            ))
            .collect(Collectors.toList());
    }

    public List<RevisaoResponse> listarRevisoesVencidas() {
        return repositorio.listarVencidas(LocalDate.now()).stream()
            .map(r -> new RevisaoResponse(
                r.getId(),
                r.getTopicoId(),
                r.getNumeroRevisao(),
                r.getDataPrevista(),
                r.getDataRealizada(),
                r.getStatus(),
                r.getCriadaEm()
            ))
            .collect(Collectors.toList());
    }

    public void criarRevisao(Long topicoId, int numeroRevisao, LocalDate dataPrevista) {
        repositorio.criarRevisao(topicoId, numeroRevisao, dataPrevista);
    }

    public RevisaoResponse marcarComoRealizada(Long id, LocalDate dataRealizada) {
        if (dataRealizada == null) {
            dataRealizada = LocalDate.now();
        }

        repositorio.marcarComoRealizada(id, dataRealizada);

        Revisao revisao = repositorio.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Revisão não encontrada com ID " + id));

        return new RevisaoResponse(
            revisao.getId(),
            revisao.getTopicoId(),
            revisao.getNumeroRevisao(),
            revisao.getDataPrevista(),
            revisao.getDataRealizada(),
            revisao.getStatus(),
            revisao.getCriadaEm()
        );
    }

    public List<RevisaoResponse> listarRevisoesDoDia() {
    LocalDate hoje = LocalDate.now();
    return repositorio.listarRevisoesDoDia(hoje).stream()
        .map(r -> new RevisaoResponse(
            r.getId(),
            r.getTopicoId(),
            r.getNumeroRevisao(),
            r.getDataPrevista(),
            r.getDataRealizada(),
            r.getStatus(),
            r.getCriadaEm()
        ))
        .collect(Collectors.toList());
    }
    public DashboardResponse getDashboard() {
        List<Revisao> todas = repositorio.listarTodas();

        long totalPendentes = todas.stream()
            .filter(r -> r.getStatus() == StatusRevisao.PENDENTE)
            .count();

        long totalConcluidas = todas.stream()
            .filter(r -> r.getStatus() == StatusRevisao.CONCLUIDA)
            .count();

        long totalVencidas = todas.stream()
            .filter(r -> r.getStatus() == StatusRevisao.VENCIDA)
            .count();

        long total = todas.size();
        double percentualConclusao =
            total > 0 ? (totalConcluidas * 100.0) / total : 0;

        return new DashboardResponse(
            totalPendentes,
            totalConcluidas,
            totalVencidas,
            percentualConclusao
        );
    }
}