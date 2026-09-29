package br.rildo.studyflow.domain;

import br.rildo.studyflow.domain.enums.DiaSemana;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "config_dia_semana")
public class ConfigDiaSemana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long preparacaoId;

    @NotNull
    private DiaSemana diaSemana;

    @PositiveOrZero
    private int minutosDiarios;

    public ConfigDiaSemana() {
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

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getMinutosDiarios() {
        return minutosDiarios;
    }

    public void setMinutosDiarios(int minutosDiarios) {
        this.minutosDiarios = minutosDiarios;
    }
}