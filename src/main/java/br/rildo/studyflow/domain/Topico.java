package br.rildo.studyflow.domain;

import java.time.LocalDateTime;

import br.rildo.studyflow.domain.enums.StatusTopico;

public class Topico {
    private Long id;
    private Long preparacaoId;
    private String nome;
    private StatusTopico status;
    private int peso;
    private LocalDateTime criadoEm;

    public Topico() {
    }

    public Topico(Long id, Long preparacaoId, String nome, StatusTopico status, int peso, LocalDateTime criadoEm) {
        this.id = id;
        this.preparacaoId = preparacaoId;
        this.nome = nome;
        this.status = status;
        this.peso = peso;
        this.criadoEm = criadoEm;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public void setStatus(StatusTopico status) {
        this.status = status;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}