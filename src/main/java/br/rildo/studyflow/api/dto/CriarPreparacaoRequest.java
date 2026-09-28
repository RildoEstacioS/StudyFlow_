package br.rildo.studyflow.api.dto;

import java.time.LocalDate;

import br.rildo.studyflow.domain.enums.EstrategiaPlanejamentoTipo;
import br.rildo.studyflow.domain.enums.TipoDataAlvo;

public class CriarPreparacaoRequest {

    private String nome;
    private String banca;
    private LocalDate dataAlvo;
    private TipoDataAlvo tipoDataAlvo;
    private int minutosDisponiveisPorSemana;
    private EstrategiaPlanejamentoTipo estrategiaPlanejamento;

    public CriarPreparacaoRequest() {
    }

    public CriarPreparacaoRequest(String nome,
                                  String banca,
                                  LocalDate dataAlvo,
                                  TipoDataAlvo tipoDataAlvo,
                                  int minutosDisponiveisPorSemana,
                                  EstrategiaPlanejamentoTipo estrategiaPlanejamento) {
        this.nome = nome;
        this.banca = banca;
        this.dataAlvo = dataAlvo;
        this.tipoDataAlvo = tipoDataAlvo;
        this.minutosDisponiveisPorSemana = minutosDisponiveisPorSemana;
        this.estrategiaPlanejamento = estrategiaPlanejamento;
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
}