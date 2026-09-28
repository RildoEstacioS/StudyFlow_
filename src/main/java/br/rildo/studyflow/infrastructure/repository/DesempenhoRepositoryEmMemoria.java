package br.rildo.studyflow.infrastructure.repository;

import br.rildo.studyflow.domain.DesempenhoTopico;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DesempenhoRepositoryEmMemoria implements DesempenhoRepository {
    private final Map<Long, DesempenhoTopico> desempenhos = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(0);

    @Override
    public DesempenhoTopico salvar(DesempenhoTopico desempenho) {
        if (desempenho.getId() == null) {
            Long id = contador.incrementAndGet();
            desempenho.setId(id);
        }
        desempenho.setUltimaAtualizacao(LocalDateTime.now());
        desempenhos.put(desempenho.getTopicoId(), desempenho);
        return desempenho;
    }

    @Override
    public Optional<DesempenhoTopico> buscarPorTopico(Long topicoId) {
        return Optional.ofNullable(desempenhos.get(topicoId));
    }
}