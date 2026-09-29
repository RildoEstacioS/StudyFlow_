package br.rildo.studyflow.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;

@Service
public class PreparacaoService {

    private final PreparacaoRepositoryJpa repository;

    public PreparacaoService(PreparacaoRepositoryJpa repository) {
        this.repository = repository;
    }

    public List<Preparacao> listarTodas() {
        return repository.findAll();
    }

    public Optional<Preparacao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Preparacao criar(Preparacao preparacao) {
        return repository.save(preparacao);
    }

    public Preparacao atualizar(Long id, Preparacao preparacaoAtualizada) {
        Preparacao preparacaoExistente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        preparacaoExistente.setNome(preparacaoAtualizada.getNome());
        preparacaoExistente.setBanca(preparacaoAtualizada.getBanca());
        preparacaoExistente.setDataAlvo(preparacaoAtualizada.getDataAlvo());
        preparacaoExistente.setTipoDataAlvo(preparacaoAtualizada.getTipoDataAlvo());
        preparacaoExistente.setMinutosDisponiveisPorSemana(
                preparacaoAtualizada.getMinutosDisponiveisPorSemana()
        );
        preparacaoExistente.setEstrategiaPlanejamento(
                preparacaoAtualizada.getEstrategiaPlanejamento()
        );

        return repository.save(preparacaoExistente);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}