package br.rildo.studyflow.domain;

public enum Status {
    
    NAO_INICIADO("Não Iniciado", "#6c757d", "bg-secondary"),
    EM_PROGRESSO("Em Progresso", "#3498db", "bg-primary"),
    CONCLUIDO("Concluído", "#27ae60", "bg-success"),
    ATRASADO("Atrasado", "#e74c3c", "bg-danger");
    
    private final String label;
    private final String corHex;
    private final String classeBootstrap;
    
    Status(String label, String corHex, String classeBootstrap) {
        this.label = label;
        this.corHex = corHex;
        this.classeBootstrap = classeBootstrap;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getCorHex() {
        return corHex;
    }
    
    public String getClasseBootstrap() {
        return classeBootstrap;
    }
}