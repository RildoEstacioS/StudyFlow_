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
@Table(name = "sessao_estudo")
public class SessaoEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long topicoId;

    @PositiveOrZero
    private int questoesRespondidas;

    @PositiveOrZero
    private int acertos;

    @NotNull
    private LocalDateTime dataSessao;

    public SessaoEstudo() {
    }

    public SessaoEstudo(
            Long topicoId,
            int questoesRespondidas,
            int acertos,
            LocalDateTime dataSessao
    ) {
        this.topicoId = topicoId;
        this.questoesRespondidas = questoesRespondidas;
        this.acertos = acertos;
        this.dataSessao = dataSessao;
    }

    public SessaoEstudo(
            Long id,
            Long topicoId,
            int questoesRespondidas,
            int acertos,
            LocalDateTime dataSessao
    ) {
        this.id = id;
        this.topicoId = topicoId;
        this.questoesRespondidas = questoesRespondidas;
        this.acertos = acertos;
        this.dataSessao = dataSessao;
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

    public LocalDateTime getDataSessao() {
        return dataSessao;
    }

    public void setDataSessao(LocalDateTime dataSessao) {
        this.dataSessao = dataSessao;
    }

    public int getPercentualAcertos() {
        if (questoesRespondidas == 0) {
            return 0;
        }
        return (acertos * 100) / questoesRespondidas;
    }
}