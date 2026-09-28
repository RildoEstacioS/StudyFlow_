package br.rildo.studyflow.api.controller;

import br.rildo.studyflow.api.dto.CriarTopicoRequest;
import br.rildo.studyflow.api.dto.TopicoResponse;
import br.rildo.studyflow.application.service.TopicoService;
import br.rildo.studyflow.infrastructure.repository.TopicoRepository;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryEmMemoria;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/preparacoes/{preparacaoId}/topicos")
public class TopicoController {
    private final TopicoService service;

    public TopicoController() {
        TopicoRepository repositorio = new TopicoRepositoryEmMemoria();
        this.service = new TopicoService(repositorio);
    }

    @PostMapping
    public ResponseEntity<TopicoResponse> criarTopico(
        @PathVariable Long preparacaoId,
        @RequestBody CriarTopicoRequest request
    ) {
        TopicoResponse response = service.criarTopico(preparacaoId, request);
        URI uri = URI.create("/api/preparacoes/" + preparacaoId + "/topicos");
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TopicoResponse>> listarTopicos(@PathVariable Long preparacaoId) {
        List<TopicoResponse> topicos = service.listarTopicosPorPreparacao(preparacaoId);
        return ResponseEntity.ok(topicos);
    }
}