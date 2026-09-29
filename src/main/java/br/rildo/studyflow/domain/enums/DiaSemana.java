package br.rildo.studyflow.domain.enums;

public enum DiaSemana {
    DOMINGO(0),
    SEGUNDA(1),
    TERCA(2),
    QUARTA(3),
    QUINTA(4),
    SEXTA(5),
    SABADO(6);

    private final int valor;

    DiaSemana(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static DiaSemana deValor(int valor) {
        for (DiaSemana d : DiaSemana.values()) {
            if (d.valor == valor) {
                return d;
            }
        }
        throw new IllegalArgumentException("Dia da semana inválido: " + valor);
    }
}