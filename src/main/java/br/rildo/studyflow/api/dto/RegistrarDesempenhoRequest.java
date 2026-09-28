package br.rildo.studyflow.api.dto;

public class RegistrarDesempenhoRequest {
    private int questoesRespondidas;
    private int percentualAcertos;

    public RegistrarDesempenhoRequest() {
    }

    public RegistrarDesempenhoRequest(int questoesRespondidas, int percentualAcertos) {
        this.questoesRespondidas = questoesRespondidas;
        this.percentualAcertos = percentualAcertos;
    }

    public int getQuestoesRespondidas() {
        return questoesRespondidas;
    }

    public void setQuestoesRespondidas(int questoesRespondidas) {
        this.questoesRespondidas = questoesRespondidas;
    }

    public int getPercentualAcertos() {
        return percentualAcertos;
    }

    public void setPercentualAcertos(int percentualAcertos) {
        this.percentualAcertos = percentualAcertos;
    }
}