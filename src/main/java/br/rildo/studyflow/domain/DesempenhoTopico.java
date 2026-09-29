package br.rildo.studyflow.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "desempenho_topico")
public class DesempenhoTopico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long topicoId;

    @PositiveOrZero
    private int questoesRespondidas;

    @PositiveOrZero
    private int acertos;

    @PositiveOrZero
    private int percentualAcertos;

    @NotNull
    private LocalDateTime ultimaAtualizacao;

    public DesempenhoTopico() {
    }

    public DesempenhoTopico(
            Long topicoId,
            int questoesRespondidas,
            int acertos,
            int percentualAcertos
    ) {
        this.topicoId = topicoId;
        this.questoesRespondidas = questoesRespondidas;
        this.acertos = acertos;
        this.percentualAcertos = percentualAcertos;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public DesempenhoTopico(
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