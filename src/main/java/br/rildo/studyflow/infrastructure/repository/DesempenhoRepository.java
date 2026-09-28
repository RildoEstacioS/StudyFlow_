package br.rildo.studyflow.infrastructure.repository;

import br.rildo.studyflow.domain.DesempenhoTopico;

import java.util.Optional;

public interface DesempenhoRepository {
    DesempenhoTopico salvar(DesempenhoTopico desempenho);
    Optional<DesempenhoTopico> buscarPorTopico(Long topicoId);
}