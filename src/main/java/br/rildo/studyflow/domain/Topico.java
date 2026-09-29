package br.rildo.studyflow.domain;

import java.time.LocalDateTime;

import br.rildo.studyflow.domain.enums.StatusTopico;
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
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long preparacaoId;

    private Long disciplinaId;

    @NotBlank
    @Column(length = 150)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private StatusTopico status;

    @Positive
    private int peso;

    @NotNull
    private LocalDateTime criadoEm;

    public Topico() {
    }

    public Topico(
            Long preparacaoId,
            String nome,
            StatusTopico status,
            int peso
    ) {
        this.preparacaoId = preparacaoId;
        this.nome = nome;
        this.status = status;
        this.peso = peso;
        this.criadoEm = LocalDateTime.now();
    }

    public Topico(
            Long id,
            Long preparacaoId,
            String nome,
            StatusTopico status,
            int peso,
            LocalDateTime criadoEm
    ) {
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

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
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