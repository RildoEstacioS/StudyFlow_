package br.rildo.studyflow.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rildo.studyflow.api.dto.CriarPreparacaoRequest;
import br.rildo.studyflow.api.dto.PreparacaoResponse;
import br.rildo.studyflow.application.service.PreparacaoService;

@RestController
@RequestMapping("/api/preparacoes")
public class PreparacaoController {

    private final PreparacaoService service;

    public PreparacaoController(PreparacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PreparacaoResponse> criar(@RequestBody CriarPreparacaoRequest request) {
        PreparacaoResponse resposta = service.criar(request);
        URI localizacao = URI.create("/api/preparacoes/" + resposta.getId());
        return ResponseEntity.created(localizacao).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<PreparacaoResponse>> listarTodas() {
        List<PreparacaoResponse> todas = service.listarTodas();
        return ResponseEntity.ok(todas);
    }
}