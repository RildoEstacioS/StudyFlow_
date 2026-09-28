package br.rildo.studyflow.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.rildo.studyflow.api.dto.DesempenhoResponse;
import br.rildo.studyflow.api.dto.RegistrarDesempenhoRequest;
import br.rildo.studyflow.domain.DesempenhoTopico;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepository;

public class DesempenhoService {
    private final DesempenhoRepository repositorio;
    private final RevisaoService revisaoService;

    public DesempenhoService(DesempenhoRepository repositorio, RevisaoService revisaoService) {
        this.repositorio = repositorio;
        this.revisaoService = revisaoService;
    }

    public DesempenhoResponse registrarDesempenho(Long topicoId, RegistrarDesempenhoRequest request) {
        if (request.getQuestoesRespondidas() <= 0) {
            throw new IllegalArgumentException("Questões respondidas deve ser maior que zero");
        }

        if (request.getPercentualAcertos() < 0 || request.getPercentualAcertos() > 100) {
            throw new IllegalArgumentException("Percentual de acertos deve ser entre 0 e 100");
        }

        DesempenhoTopico desempenho = new DesempenhoTopico();
        desempenho.setTopicoId(topicoId);
        desempenho.setQuestoesRespondidas(request.getQuestoesRespondidas());
        desempenho.setPercentualAcertos(request.getPercentualAcertos());
        desempenho.setUltimaAtualizacao(LocalDateTime.now());

        DesempenhoTopico salvo = repositorio.salvar(desempenho);

        // Criar revisões automáticas baseadas no desempenho
        criarRevisoesAutomaticas(topicoId, request.getPercentualAcertos());

        return new DesempenhoResponse(
            salvo.getId(),
            salvo.getTopicoId(),
            salvo.getQuestoesRespondidas(),
            salvo.getPercentualAcertos(),
            salvo.getUltimaAtualizacao()
        );
    }

    private void criarRevisoesAutomaticas(Long topicoId, int percentualAcertos) {
        // Intervalos baseados no desempenho (metodologia Foco no Papiro)
        int[] intervalosDias;

        if (percentualAcertos <= 50) {
            // Desempenho baixo: revisar mais cedo
            intervalosDias = new int[]{1, 3, 7, 15};
        } else if (percentualAcertos <= 70) {
            // Desempenho médio
            intervalosDias = new int[]{1, 7, 15, 30};
        } else if (percentualAcertos <= 85) {
            // Desempenho bom
            intervalosDias = new int[]{7, 15, 30, 60};
        } else {
            // Desempenho excelente: revisar mais tarde
            intervalosDias = new int[]{15, 30, 60, 90};
        }

        LocalDate hoje = LocalDate.now();

        for (int i = 0; i < intervalosDias.length; i++) {
            LocalDate dataPrevista = hoje.plusDays(intervalosDias[i]);
            revisaoService.criarRevisao(topicoId, i + 1, dataPrevista);
        }
    }

    public DesempenhoResponse buscarDesempenhoPorTopico(Long topicoId) {
        return repositorio.buscarPorTopico(topicoId)
            .map(d -> new DesempenhoResponse(
                d.getId(),
                d.getTopicoId(),
                d.getQuestoesRespondidas(),
                d.getPercentualAcertos(),
                d.getUltimaAtualizacao()
            ))
            .orElseThrow(() -> new IllegalArgumentException("Desempenho não encontrado para o tópico " + topicoId));
    }
}