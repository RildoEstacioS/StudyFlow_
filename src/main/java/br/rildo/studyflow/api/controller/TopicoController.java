package br.rildo.studyflow.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rildo.studyflow.api.dto.CriarTopicoRequest;
import br.rildo.studyflow.api.dto.TopicoResponse;
import br.rildo.studyflow.application.service.TopicoService;
import br.rildo.studyflow.infrastructure.repository.TopicoRepository;

@RestController
@RequestMapping("/api/preparacoes/{preparacaoId}/topicos")
public class TopicoController {

    private final TopicoService service;

    public TopicoController(TopicoRepository repositorio) {
        this.service = new TopicoService(repositorio);
    }

    @PostMapping
    public ResponseEntity<TopicoResponse> criarTopico(
            @PathVariable Long preparacaoId,
            @RequestBody CriarTopicoRequest request
    ) {
        TopicoResponse response = service.criarTopico(preparacaoId, request);

        URI uri = URI.create(
                "/api/preparacoes/" + preparacaoId
                        + "/topicos/" + response.getId()
        );

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TopicoResponse>> listarTopicos(
            @PathVariable Long preparacaoId
    ) {
        return ResponseEntity.ok(
                service.listarTopicosPorPreparacao(preparacaoId)
        );
    }
}