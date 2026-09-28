package br.rildo.studyflow.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.rildo.studyflow.api.dto.CriarPreparacaoRequest;
import br.rildo.studyflow.api.dto.PreparacaoResponse;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepository;

@Service
public class PreparacaoService {

    private final PreparacaoRepository repository;

    public PreparacaoService(PreparacaoRepository repository) {
        this.repository = repository;
    }

    public PreparacaoResponse criar(CriarPreparacaoRequest request) {
        Preparacao preparacao = new Preparacao();
        preparacao.setNome(request.getNome());
        preparacao.setBanca(request.getBanca());
        preparacao.setDataAlvo(request.getDataAlvo());
        preparacao.setTipoDataAlvo(request.getTipoDataAlvo());
        preparacao.setMinutosDisponiveisPorSemana(request.getMinutosDisponiveisPorSemana());
        preparacao.setEstrategiaPlanejamento(request.getEstrategiaPlanejamento());

        Preparacao salva = repository.salvar(preparacao);
        return paraResponse(salva);
    }

    public List<PreparacaoResponse> listarTodas() {
        List<Preparacao> todas = repository.listarTodos();
        return todas.stream()
                .map(this::paraResponse)
                .collect(Collectors.toList());
    }

    private PreparacaoResponse paraResponse(Preparacao preparacao) {
        return new PreparacaoResponse(
                preparacao.getId(),
                preparacao.getNome(),
                preparacao.getBanca(),
                preparacao.getDataAlvo(),
                preparacao.getTipoDataAlvo(),
                preparacao.getMinutosDisponiveisPorSemana(),
                preparacao.getEstrategiaPlanejamento(),
                preparacao.getCriadaEm()
        );
    }
}