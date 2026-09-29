package br.rildo.studyflow.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.rildo.studyflow.domain.enums.TipoTarefa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tarefa_estudo")
public class TarefaEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long preparacaoId;

    private Long topicoId; // null para simulado geral

    @NotNull
    private LocalDate data;

    @NotNull
    private TipoTarefa tipo;

    private Integer numeroRevisao; // para REVISAO

    private Boolean concluida = false;

    private LocalDateTime concluidaEm;

    public TarefaEstudo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPreparacaoId() {
        return preparacaoId;
    }

    public void setPreparacaoId(Long preparacaoId) {
        this.preparacaoId = preparacaoId;
    }

    public Long getTopicoId() {
        return topicoId;
    }

    public void setTopicoId(Long topicoId) {
        this.topicoId = topicoId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public TipoTarefa getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarefa tipo) {
        this.tipo = tipo;
    }

    public Integer getNumeroRevisao() {
        return numeroRevisao;
    }

    public void setNumeroRevisao(Integer numeroRevisao) {
        this.numeroRevisao = numeroRevisao;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDateTime getConcluidaEm() {
        return concluidaEm;
    }

    public void setConcluidaEm(LocalDateTime concluidaEm) {
        this.concluidaEm = concluidaEm;
    }
}