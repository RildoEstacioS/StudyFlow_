package br.rildo.studyflow.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.rildo.studyflow.domain.enums.StatusRevisao;

public class RevisaoResponse {
    private Long id;
    private Long topicoId;
    private int numeroRevisao;
    private LocalDate dataPrevista;
    private LocalDate dataRealizada;
    private StatusRevisao status;
    private LocalDateTime criadaEm;

    public RevisaoResponse() {
    }

    public RevisaoResponse(Long id, Long topicoId, int numeroRevisao, LocalDate dataPrevista, LocalDate dataRealizada, StatusRevisao status, LocalDateTime criadaEm) {
        this.id = id;
        this.topicoId = topicoId;
        this.numeroRevisao = numeroRevisao;
        this.dataPrevista = dataPrevista;
        this.dataRealizada = dataRealizada;
        this.status = status;
        this.criadaEm = criadaEm;
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

    public int getNumeroRevisao() {
        return numeroRevisao;
    }

    public void setNumeroRevisao(int numeroRevisao) {
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