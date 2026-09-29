package br.rildo.studyflow.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Transactional
    public void atualizar(Long id, Preparacao dados) {
        Preparacao preparacao = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Preparação não encontrada"));
        
        // Validar data
        if (dados.getDataAlvo() != null && dados.getDataAlvo().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data não pode ser no passado");
        }
        
        // Validar minutos
        if (dados.getMinutosDisponiveisPorSemana() != null) {
            if (dados.getMinutosDisponiveisPorSemana() < 60) {
                throw new IllegalArgumentException("Mínimo 60 minutos por semana");
            }
            if (dados.getMinutosDisponiveisPorSemana() > 5040) {
                throw new IllegalArgumentException("Máximo 5040 minutos por semana");
            }
        }
        
        // Atualizar campos
        preparacao.setNome(dados.getNome());
        preparacao.setBanca(dados.getBanca());
        preparacao.setDataAlvo(dados.getDataAlvo());
        preparacao.setTipoDataAlvo(dados.getTipoDataAlvo());
        preparacao.setMinutosDisponiveisPorSemana(dados.getMinutosDisponiveisPorSemana());
        preparacao.setEstrategiaPlanejamento(dados.getEstrategiaPlanejamento());
        
        repository.save(preparacao);
    }
    
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}