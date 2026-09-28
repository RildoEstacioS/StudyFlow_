package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.rildo.studyflow.domain.Preparacao;

@Repository
public class PreparacaoRepositoryEmMemoria implements PreparacaoRepository {

    private final Map<Long, Preparacao> armazenagem = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public Preparacao salvar(Preparacao preparacao) {
        if (preparacao.getId() == null) {
            Long id = contador.getAndIncrement();
            preparacao.setId(id);
            preparacao.setCriadaEm(LocalDateTime.now());
        }
        armazenagem.put(preparacao.getId(), preparacao);
        return preparacao;
    }

    @Override
    public List<Preparacao> listarTodos() {
        return new ArrayList<>(armazenagem.values());
    }

    @Override
    public Optional<Preparacao> buscarPorId(Long id) {
        return Optional.ofNullable(armazenagem.get(id));
    }

    @Override
    public void deletar(Long id) {
        armazenagem.remove(id);
    }
}