package br.rildo.studyflow.api.dto;

import java.time.LocalDate;

import br.rildo.studyflow.domain.enums.EstrategiaPlanejamentoTipo;
import br.rildo.studyflow.domain.enums.TipoDataAlvo;

public class PreparacaoDTO {
    
    private String nome;
    private String banca;
    private LocalDate dataAlvo;
    private TipoDataAlvo tipoDataAlvo;
    private Integer minutosDisponiveisPorSemana;
    private EstrategiaPlanejamentoTipo estrategiaPlanejamento;
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getBanca() { return banca; }
    public void setBanca(String banca) { this.banca = banca; }
    
    public LocalDate getDataAlvo() { return dataAlvo; }
    public void setDataAlvo(LocalDate dataAlvo) { this.dataAlvo = dataAlvo; }
    
    public TipoDataAlvo getTipoDataAlvo() { return tipoDataAlvo; }
    public void setTipoDataAlvo(TipoDataAlvo tipoDataAlvo) { this.tipoDataAlvo = tipoDataAlvo; }
    
    public Integer getMinutosDisponiveisPorSemana() { return minutosDisponiveisPorSemana; }
    public void setMinutosDisponiveisPorSemana(Integer minutosDisponiveisPorSemana) { 
        this.minutosDisponiveisPorSemana = minutosDisponiveisPorSemana; 
    }
    
    public EstrategiaPlanejamentoTipo getEstrategiaPlanejamento() { return estrategiaPlanejamento; }
    public void setEstrategiaPlanejamento(EstrategiaPlanejamentoTipo estrategiaPlanejamento) { 
        this.estrategiaPlanejamento = estrategiaPlanejamento; 
    }
}