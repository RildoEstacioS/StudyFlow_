package br.rildo.studyflow.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long preparacaoId;

    @NotBlank
    @Column(length = 120)
    private String nome;

    @Positive
    private int peso;

    @NotNull
    private LocalDateTime criadaEm;

    public Disciplina() {
    }

    public Disciplina(
            Long preparacaoId,
            String nome,
            int peso
    ) {
        this.preparacaoId = preparacaoId;
        this.nome = nome;
        this.peso = peso;
        this.criadaEm = LocalDateTime.now();
    }

    public Disciplina(
            Long id,
            Long preparacaoId,
            String nome,
            int peso,
            LocalDateTime criadaEm
    ) {
        this.id = id;
        this.preparacaoId = preparacaoId;
        this.nome = nome;
        this.peso = peso;
        this.criadaEm = criadaEm;
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

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
}