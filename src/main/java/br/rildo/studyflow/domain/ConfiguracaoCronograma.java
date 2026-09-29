package br.rildo.studyflow.domain;

import br.rildo.studyflow.domain.enums.FrequenciaSimulado;
import br.rildo.studyflow.domain.enums.TipoRevisao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "configuracao_cronograma")
public class ConfiguracaoCronograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long preparacaoId;

    @Positive
    private int materiasPorDia = 1;

    @NotNull
    private FrequenciaSimulado frequenciaSimulado = FrequenciaSimulado.NENHUM;

    private Integer diaPreferencialSimulado; // 0-6, null = indiferente

    @NotNull
    private TipoRevisao tipoRevisao = TipoRevisao.POR_DESEMPENHO;

    private Integer intervaloRevisaoDias; // usado se tipoRevisao = POR_INTERVALO_FIXO

    public ConfiguracaoCronograma() {
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

    public int getMateriasPorDia() {
        return materiasPorDia;
    }

    public void setMateriasPorDia(int materiasPorDia) {
        this.materiasPorDia = materiasPorDia;
    }

    public FrequenciaSimulado getFrequenciaSimulado() {
        return frequenciaSimulado;
    }

    public void setFrequenciaSimulado(FrequenciaSimulado frequenciaSimulado) {
        this.frequenciaSimulado = frequenciaSimulado;
    }

    public Integer getDiaPreferencialSimulado() {
        return diaPreferencialSimulado;
    }

    public void setDiaPreferencialSimulado(Integer diaPreferencialSimulado) {
        this.diaPreferencialSimulado = diaPreferencialSimulado;
    }

    public TipoRevisao getTipoRevisao() {
        return tipoRevisao;
    }

    public void setTipoRevisao(TipoRevisao tipoRevisao) {
        this.tipoRevisao = tipoRevisao;
    }

    public Integer getIntervaloRevisaoDias() {
        return intervaloRevisaoDias;
    }

    public void setIntervaloRevisaoDias(Integer intervaloRevisaoDias) {
        this.intervaloRevisaoDias = intervaloRevisaoDias;
    }
}