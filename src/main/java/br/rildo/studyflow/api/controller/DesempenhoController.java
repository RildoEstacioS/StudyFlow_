package br.rildo.studyflow.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rildo.studyflow.api.dto.DesempenhoResponse;
import br.rildo.studyflow.api.dto.RegistrarDesempenhoRequest;
import br.rildo.studyflow.application.service.DesempenhoService;
import br.rildo.studyflow.application.service.RevisaoService;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.SessaoEstudoRepositoryJpa;

@RestController
@RequestMapping("/api/topicos/{topicoId}/desempenho")
public class DesempenhoController {

    private final DesempenhoService service;

    public DesempenhoController(
            DesempenhoRepositoryJpa repositorioDesempenho,
            RevisaoRepositoryJpa repositorioRevisao,
            SessaoEstudoRepositoryJpa repositorioSessao
    ) {
        RevisaoService revisaoService = new RevisaoService(repositorioRevisao);
        this.service = new DesempenhoService(
            repositorioDesempenho,
            repositorioSessao,
            revisaoService
        );
    }

    @PostMapping
    public ResponseEntity<DesempenhoResponse> registrarDesempenho(
            @PathVariable Long topicoId,
            @RequestBody RegistrarDesempenhoRequest request
    ) {
        DesempenhoResponse response = service.registrarDesempenho(topicoId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DesempenhoResponse> buscarDesempenho(
            @PathVariable Long topicoId
    ) {
        DesempenhoResponse response = service.buscarDesempenhoPorTopico(topicoId);
        return ResponseEntity.ok(response);
    }
}