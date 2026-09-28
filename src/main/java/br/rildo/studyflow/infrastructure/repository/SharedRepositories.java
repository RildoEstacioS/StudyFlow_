package br.rildo.studyflow.infrastructure.repository;

public class SharedRepositories {
    private static final DesempenhoRepositoryEmMemoria desempenhoRepository = new DesempenhoRepositoryEmMemoria();
    private static final RevisaoRepositoryEmMemoria revisaoRepository = new RevisaoRepositoryEmMemoria();

    public static DesempenhoRepositoryEmMemoria getDesempenhoRepository() {
        return desempenhoRepository;
    }

    public static RevisaoRepositoryEmMemoria getRevisaoRepository() {
        return revisaoRepository;
    }
}