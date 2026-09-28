package br.rildo.studyflow.application.service;

import java.time.LocalDateTime;
import java.util.List;

import br.rildo.studyflow.api.dto.CriarTopicoRequest;
import br.rildo.studyflow.api.dto.TopicoResponse;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.domain.enums.StatusTopico;
import br.rildo.studyflow.infrastructure.repository.TopicoRepository;

public class TopicoService {
    private final TopicoRepository repositorio;

    public TopicoService(TopicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public TopicoResponse criarTopico(Long preparacaoId, CriarTopicoRequest request) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do tópico é obrigatório");
        }

        if (request.getPeso() <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }

        Topico topico = new Topico();
        topico.setPreparacaoId(preparacaoId);
        topico.setNome(request.getNome());
        topico.setStatus(request.getStatus() != null ? request.getStatus() : StatusTopico.PENDENTE);
        topico.setPeso(request.getPeso());
        topico.setCriadoEm(LocalDateTime.now());

        Topico salvo = repositorio.salvar(topico);

        return new TopicoResponse(
            salvo.getId(),
            salvo.getPreparacaoId(),
            salvo.getNome(),
            salvo.getStatus(),
            salvo.getPeso(),
            salvo.getCriadoEm()
        );
    }

    public List<TopicoResponse> listarTopicosPorPreparacao(Long preparacaoId) {
        List<Topico> topicos = repositorio.listarPorPreparacao(preparacaoId);
        return topicos.stream()
            .map(t -> new TopicoResponse(
                t.getId(),
                t.getPreparacaoId(),
                t.getNome(),
                t.getStatus(),
                t.getPeso(),
                t.getCriadoEm()
            ))
            .toList();
    }
}