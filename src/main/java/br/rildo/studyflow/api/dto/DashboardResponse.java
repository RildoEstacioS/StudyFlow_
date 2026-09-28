package br.rildo.studyflow.api.dto;

public class DashboardResponse {
    private final long totalPendentes;
    private final long totalConcluidas;
    private final long totalVencidas;
    private final double percentualConclusao;

    public DashboardResponse(long totalPendentes, long totalConcluidas, long totalVencidas, double percentualConclusao) {
        this.totalPendentes = totalPendentes;
        this.totalConcluidas = totalConcluidas;
        this.totalVencidas = totalVencidas;
        this.percentualConclusao = percentualConclusao;
    }
    // Getters
    public long getTotalPendentes() {
        return totalPendentes;
    }

    public long getTotalConcluidas() {
        return totalConcluidas;
    }

    public long getTotalVencidas() {
        return totalVencidas;
    }

    public double getPercentualConclusao() {
        return percentualConclusao;
    }
}
