package br.rildo.studyflow.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rildo.studyflow.application.service.PreparacaoService;
import br.rildo.studyflow.domain.Preparacao;

@RestController
@RequestMapping("/api/preparacoes")
public class PreparacaoController {

    private final PreparacaoService service;

    public PreparacaoController(PreparacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Preparacao> criar(@RequestBody Preparacao preparacao) {
        Preparacao salva = service.criar(preparacao);
        URI localizacao = URI.create("/api/preparacoes/" + salva.getId());
        return ResponseEntity.created(localizacao).body(salva);
    }

    @GetMapping
    public ResponseEntity<List<Preparacao>> listarTodas() {
        List<Preparacao> todas = service.listarTodas();
        return ResponseEntity.ok(todas);
    }
}