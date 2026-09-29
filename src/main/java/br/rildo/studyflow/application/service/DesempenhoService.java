package br.rildo.studyflow.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.rildo.studyflow.api.dto.DesempenhoResponse;
import br.rildo.studyflow.api.dto.RegistrarDesempenhoRequest;
import br.rildo.studyflow.domain.DesempenhoTopico;
import br.rildo.studyflow.domain.SessaoEstudo;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.SessaoEstudoRepositoryJpa;

public class DesempenhoService {

    private final DesempenhoRepositoryJpa repositorioDesempenho;
    private final SessaoEstudoRepositoryJpa repositorioSessao;
    private final RevisaoService revisaoService;

    public DesempenhoService(
            DesempenhoRepositoryJpa repositorioDesempenho,
            SessaoEstudoRepositoryJpa repositorioSessao,
            RevisaoService revisaoService
    ) {
        this.repositorioDesempenho = repositorioDesempenho;
        this.repositorioSessao = repositorioSessao;
        this.revisaoService = revisaoService;
    }

    /**
     * Registra desempenho usando percentual direto (API REST).
     * Mantém compatibilidade com o controller REST existente.
     */
    public DesempenhoResponse registrarDesempenho(
            Long topicoId,
            RegistrarDesempenhoRequest request
    ) {
        if (request.getQuestoesRespondidas() <= 0) {
            throw new IllegalArgumentException(
                "Questões respondidas deve ser maior que zero"
            );
        }

        if (request.getPercentualAcertos() < 0
                || request.getPercentualAcertos() > 100) {
            throw new IllegalArgumentException(
                "Percentual de acertos deve ser entre 0 e 100"
            );
        }

        // Para API REST, vamos assumir acertos = percentual * questões / 100
        int acertos = (request.getPercentualAcertos() * request.getQuestoesRespondidas()) / 100;

        DesempenhoTopico desempenho = new DesempenhoTopico();
        desempenho.setTopicoId(topicoId);
        desempenho.setQuestoesRespondidas(request.getQuestoesRespondidas());
        desempenho.setAcertos(acertos);
        desempenho.setPercentualAcertos(request.getPercentualAcertos());
        desempenho.setUltimaAtualizacao(LocalDateTime.now());

        DesempenhoTopico salvo = repositorioDesempenho.salvar(desempenho);

        criarRevisoesAutomaticas(topicoId, request.getPercentualAcertos());

        return new DesempenhoResponse(
            salvo.getId(),
            salvo.getTopicoId(),
            salvo.getQuestoesRespondidas(),
            salvo.getAcertos(),
            salvo.getPercentualAcertos(),
            salvo.getUltimaAtualizacao()
        );
    }

    /**
     * Registra uma sessão de estudo com questões e acertos brutos.
     * Usado pelo formulário web de desempenho.
     */
    public DesempenhoResponse registrarSessao(
            Long topicoId,
            int questoesRespondidas,
            int acertos
    ) {
        if (questoesRespondidas <= 0) {
            throw new IllegalArgumentException(
                "Questões respondidas deve ser maior que zero"
            );
        }

        if (acertos < 0) {
            throw new IllegalArgumentException(
                "Acertos não pode ser negativo"
            );
        }

        if (acertos > questoesRespondidas) {
            throw new IllegalArgumentException(
                "Acertos não pode ser maior que questões respondidas"
            );
        }

        int percentual = (acertos * 100) / questoesRespondidas;

        // 1) Criar sessão
        SessaoEstudo sessao = new SessaoEstudo(
            topicoId,
            questoesRespondidas,
            acertos,
            LocalDateTime.now()
        );
        repositorioSessao.salvar(sessao);

        // 2) Atualizar DesempenhoTopico (última sessão)
        DesempenhoTopico desempenho = new DesempenhoTopico(
            topicoId,
            questoesRespondidas,
            acertos,
            percentual
        );
        repositorioDesempenho.salvar(desempenho);

        // 3) Criar revisões automáticas
        criarRevisoesAutomaticas(topicoId, percentual);

        return new DesempenhoResponse(
            desempenho.getId(),
            desempenho.getTopicoId(),
            desempenho.getQuestoesRespondidas(),
            desempenho.getAcertos(),
            desempenho.getPercentualAcertos(),
            desempenho.getUltimaAtualizacao()
        );
    }

    private void criarRevisoesAutomaticas(
            Long topicoId,
            int percentualAcertos
    ) {
        int[] intervalosDias;

        if (percentualAcertos <= 50) {
            intervalosDias = new int[]{1, 3, 7, 15};
        } else if (percentualAcertos <= 70) {
            intervalosDias = new int[]{1, 7, 15, 30};
        } else if (percentualAcertos <= 85) {
            intervalosDias = new int[]{7, 15, 30, 60};
        } else {
            intervalosDias = new int[]{15, 30, 60, 90};
        }

        LocalDate hoje = LocalDate.now();

        for (int i = 0; i < intervalosDias.length; i++) {
            LocalDate dataPrevista = hoje.plusDays(intervalosDias[i]);
            revisaoService.criarRevisao(topicoId, i + 1, dataPrevista);
        }
    }

    public DesempenhoResponse buscarDesempenhoPorTopico(Long topicoId) {
        return repositorioDesempenho.buscarPorTopico(topicoId)
            .map(d -> new DesempenhoResponse(
                d.getId(),
                d.getTopicoId(),
                d.getQuestoesRespondidas(),
                d.getAcertos(),
                d.getPercentualAcertos(),
                d.getUltimaAtualizacao()
            ))
            .orElseThrow(() -> new IllegalArgumentException(
                "Desempenho não encontrado para o tópico " + topicoId
            ));
    }
}