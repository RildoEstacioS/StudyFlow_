package br.rildo.studyflow.api.dto;

import java.time.LocalDateTime;

public class DesempenhoResponse {
    private Long id;
    private Long topicoId;
    private int questoesRespondidas;
    private int acertos;
    private int percentualAcertos;
    private LocalDateTime ultimaAtualizacao;

    public DesempenhoResponse() {
    }

    public DesempenhoResponse(
            Long id,
            Long topicoId,
            int questoesRespondidas,
            int acertos,
            int percentualAcertos,
            LocalDateTime ultimaAtualizacao
    ) {
        this.id = id;
        this.topicoId = topicoId;
        this.questoesRespondidas = questoesRespondidas;
        this.acertos = acertos;
        this.percentualAcertos = percentualAcertos;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTopicoId() {
        return topicoId;
    }

    public void setTopicoId(Long topicoId) {
        this.topicoId = topicoId;
    }

    public int getQuestoesRespondidas() {
        return questoesRespondidas;
    }

    public void setQuestoesRespondidas(int questoesRespondidas) {
        this.questoesRespondidas = questoesRespondidas;
    }

    public int getAcertos() {
        return acertos;
    }

    public void setAcertos(int acertos) {
        this.acertos = acertos;
    }

    public int getPercentualAcertos() {
        return percentualAcertos;
    }

    public void setPercentualAcertos(int percentualAcertos) {
        this.percentualAcertos = percentualAcertos;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}