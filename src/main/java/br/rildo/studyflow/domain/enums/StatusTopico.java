package br.rildo.studyflow.domain.enums;

public enum StatusTopico {
    
    NAO_INICIADO("Não Iniciado", "#6c757d", "bg-secondary"),
    EM_ESTUDO("Em Estudo", "#3498db", "bg-primary"),
    EM_REVISAO("Em Revisão", "#f39c12", "bg-warning"),
    DOMINADO("Dominado", "#27ae60", "bg-success");
    
    private final String label;
    private final String corHex;
    private final String classeBootstrap;
    
    StatusTopico(String label, String corHex, String classeBootstrap) {
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