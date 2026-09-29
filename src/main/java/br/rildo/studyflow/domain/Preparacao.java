package br.rildo.studyflow.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.rildo.studyflow.domain.enums.EstrategiaPlanejamentoTipo;
import br.rildo.studyflow.domain.enums.TipoDataAlvo;
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
@Table(name = "preparacoes")
public class Preparacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 150)
    private String nome;

    @NotBlank
    @Column(length = 100)
    private String banca;

    @NotNull
    private LocalDate dataAlvo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TipoDataAlvo tipoDataAlvo;

    @Positive
    private int minutosDisponiveisPorSemana;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private EstrategiaPlanejamentoTipo estrategiaPlanejamento;

    @NotNull
    private LocalDateTime criadaEm;

    public Preparacao() {
    }

    public Preparacao(
            String nome,
            String banca,
            LocalDate dataAlvo,
            TipoDataAlvo tipoDataAlvo,
            int minutosDisponiveisPorSemana,
            EstrategiaPlanejamentoTipo estrategiaPlanejamento
    ) {
        this.nome = nome;
        this.banca = banca;
        this.dataAlvo = dataAlvo;
        this.tipoDataAlvo = tipoDataAlvo;
        this.minutosDisponiveisPorSemana = minutosDisponiveisPorSemana;
        this.estrategiaPlanejamento = estrategiaPlanejamento;
        this.criadaEm = LocalDateTime.now();
    }

    public Preparacao(
            Long id,
            String nome,
            String banca,
            LocalDate dataAlvo,
            TipoDataAlvo tipoDataAlvo,
            int minutosDisponiveisPorSemana,
            EstrategiaPlanejamentoTipo estrategiaPlanejamento,
            LocalDateTime criadaEm
    ) {
        this.id = id;
        this.nome = nome;
        this.banca = banca;
        this.dataAlvo = dataAlvo;
        this.tipoDataAlvo = tipoDataAlvo;
        this.minutosDisponiveisPorSemana = minutosDisponiveisPorSemana;
        this.estrategiaPlanejamento = estrategiaPlanejamento;
        this.criadaEm = criadaEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBanca() {
        return banca;
    }

    public void setBanca(String banca) {
        this.banca = banca;
    }

    public LocalDate getDataAlvo() {
        return dataAlvo;
    }

    public void setDataAlvo(LocalDate dataAlvo) {
        this.dataAlvo = dataAlvo;
    }

    public TipoDataAlvo getTipoDataAlvo() {
        return tipoDataAlvo;
    }

    public void setTipoDataAlvo(TipoDataAlvo tipoDataAlvo) {
        this.tipoDataAlvo = tipoDataAlvo;
    }

    public int getMinutosDisponiveisPorSemana() {
        return minutosDisponiveisPorSemana;
    }

    public void setMinutosDisponiveisPorSemana(int minutosDisponiveisPorSemana) {
        this.minutosDisponiveisPorSemana = minutosDisponiveisPorSemana;
    }

    public EstrategiaPlanejamentoTipo getEstrategiaPlanejamento() {
        return estrategiaPlanejamento;
    }

    public void setEstrategiaPlanejamento(EstrategiaPlanejamentoTipo estrategiaPlanejamento) {
        this.estrategiaPlanejamento = estrategiaPlanejamento;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
}