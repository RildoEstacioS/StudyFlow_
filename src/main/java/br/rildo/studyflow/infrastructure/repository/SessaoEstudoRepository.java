package br.rildo.studyflow.infrastructure.repository;

import java.util.List;

import br.rildo.studyflow.domain.SessaoEstudo;

public interface SessaoEstudoRepository {
    SessaoEstudo salvar(SessaoEstudo sessao);
    List<SessaoEstudo> listarPorTopico(Long topicoId);
}