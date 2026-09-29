package br.rildo.studyflow.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.rildo.studyflow.domain.enums.StatusRevisao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "revisoes")
public class Revisao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long topicoId;

    @NotBlank
    private String topicoNome;

    @NotNull
    private Integer numeroRevisao;

    @NotNull
    private LocalDate dataPrevista;

    private LocalDate dataRealizada;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusRevisao status;

    @NotNull
    private LocalDateTime criadaEm;

    public Revisao() {
    }

    public Revisao(
            Long topicoId,
            String topicoNome,
            Integer numeroRevisao,
            LocalDate dataPrevista
    ) {
        this.topicoId = topicoId;
        this.topicoNome = topicoNome;
        this.numeroRevisao = numeroRevisao;
        this.dataPrevista = dataPrevista;
        this.status = StatusRevisao.PENDENTE;
        this.criadaEm = LocalDateTime.now();
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

    public String getTopicoNome() {
        return topicoNome;
    }

    public void setTopicoNome(String topicoNome) {
        this.topicoNome = topicoNome;
    }

    public Integer getNumeroRevisao() {
        return numeroRevisao;
    }

    public void setNumeroRevisao(Integer numeroRevisao) {
        this.numeroRevisao = numeroRevisao;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public LocalDate getDataRealizada() {
        return dataRealizada;
    }

    public void setDataRealizada(LocalDate dataRealizada) {
        this.dataRealizada = dataRealizada;
    }

    public StatusRevisao getStatus() {
        return status;
    }

    public void setStatus(StatusRevisao status) {
        this.status = status;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
}