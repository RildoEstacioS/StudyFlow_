package br.rildo.studyflow.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.rildo.studyflow.api.dto.DashboardResponse;
import br.rildo.studyflow.api.dto.RevisaoResponse;
import br.rildo.studyflow.application.service.RevisaoService;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepository;

@RestController
@RequestMapping("/api/revisoes")
public class RevisaoController {

    private final RevisaoService service;

    public RevisaoController(RevisaoRepository repositorio) {
        this.service = new RevisaoService(repositorio);
    }

    @GetMapping("/topicos/{topicoId}")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesPorTopico(
            @PathVariable Long topicoId
    ) {
        return ResponseEntity.ok(service.listarRevisoesPorTopico(topicoId));
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesPendentes() {
        return ResponseEntity.ok(service.listarRevisoesPendentes());
    }

    @GetMapping("/vencidas")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesVencidas() {
        return ResponseEntity.ok(service.listarRevisoesVencidas());
    }

    @PutMapping("/{id}/realizar")
    public ResponseEntity<RevisaoResponse> marcarComoRealizada(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.marcarComoRealizada(id, LocalDate.now()));
    }

    @GetMapping("/do-dia")
    public ResponseEntity<List<RevisaoResponse>> listarRevisoesDoDia() {
        return ResponseEntity.ok(service.listarRevisoesDoDia());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(service.getDashboard());
    }

    @PostMapping
    public ResponseEntity<Void> criarRevisao(
            @RequestParam Long topicoId,
            @RequestParam int numeroRevisao,
            @RequestParam String dataPrevista
    ) {
        service.criarRevisao(
                topicoId,
                numeroRevisao,
                LocalDate.parse(dataPrevista)
        );

        return ResponseEntity.status(201).build();
    }
    
}