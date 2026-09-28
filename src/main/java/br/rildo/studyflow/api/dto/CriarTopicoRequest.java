package br.rildo.studyflow.api.dto;

import br.rildo.studyflow.domain.enums.StatusTopico;

public class CriarTopicoRequest {
    private String nome;
    private StatusTopico status;
    private int peso;

    public CriarTopicoRequest() {
    }

    public CriarTopicoRequest(String nome, StatusTopico status, int peso) {
        this.nome = nome;
        this.status = status;
        this.peso = peso;
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
}