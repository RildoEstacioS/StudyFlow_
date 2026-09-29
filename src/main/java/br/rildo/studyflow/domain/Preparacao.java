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
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "preparacoes")
public class Preparacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Banca é obrigatória")
    @Size(max = 50, message = "Banca deve ter no máximo 50 caracteres")
    private String banca;

    @NotNull(message = "Data alvo é obrigatória")
    @Future(message = "Data alvo deve ser no futuro")
    private LocalDate dataAlvo;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    @NotNull(message = "Tipo de data é obrigatório")
    private TipoDataAlvo tipoDataAlvo;

    @NotNull(message = "Minutos semanais são obrigatórios")
    @Min(value = 60, message = "Mínimo de 60 minutos por semana")
    @Max(value = 5040, message = "Máximo de 5040 minutos (84 horas) por semana")    
    private Integer minutosDisponiveisPorSemana;

    @NotNull(message = "Estratégia é obrigatória")
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

    public Integer getMinutosDisponiveisPorSemana() {
        return minutosDisponiveisPorSemana;
    }

    public void setMinutosDisponiveisPorSemana(Integer minutosDisponiveisPorSemana) {
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