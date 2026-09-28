package br.rildo.studyflow.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rildo.studyflow.api.dto.DashboardResponse;
import br.rildo.studyflow.api.dto.RevisaoResponse;
import br.rildo.studyflow.application.service.RevisaoService;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepositoryEmMemoria;
import br.rildo.studyflow.infrastructure.repository.SharedRepositories;

@RestController
@RequestMapping("/api/revisoes")
public class RevisaoController {
    private final RevisaoService service;

    public RevisaoController() {
        RevisaoRepositoryEmMemoria repositorio = SharedRepositories.getRevisaoRepository();
        this.service = new RevisaoService(repositorio);
    }

    @GetMapping("/topicos/{topicoId}")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesPorTopico(@PathVariable Long topicoId) {
        List<RevisaoResponse> revisoes = service.listarRevisoesPorTopico(topicoId);
        return ResponseEntity.ok(revisoes);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesPendentes() {
        List<RevisaoResponse> revisoes = service.listarRevisoesPendentes();
        return ResponseEntity.ok(revisoes);
    }

    @GetMapping("/vencidas")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesVencidas() {
        List<RevisaoResponse> revisoes = service.listarRevisoesVencidas();
        return ResponseEntity.ok(revisoes);
    }

    @PutMapping("/{id}/realizar")
    public ResponseEntity<RevisaoResponse> marcarComoRealizada(@PathVariable Long id) {
        RevisaoResponse response = service.marcarComoRealizada(id, LocalDate.now());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/do-dia")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesDoDia() {
        List<RevisaoResponse> revisoes = service.listarRevisoesDoDia();
        return ResponseEntity.ok(revisoes);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        DashboardResponse dashboard = service.getDashboard();
        return ResponseEntity.ok(dashboard);
    }
}