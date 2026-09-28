package br.rildo.studyflow.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import br.rildo.studyflow.domain.Topico;

public class TopicoRepositoryEmMemoria implements TopicoRepository {
    private final List<Topico> topicos = new CopyOnWriteArrayList<>();
    private final AtomicLong contador = new AtomicLong(0);

    @Override
    public Topico salvar(Topico topico) {
        Long id = contador.incrementAndGet();
        topico.setId(id);
        topico.setCriadoEm(LocalDateTime.now());
        topicos.add(topico);
        return topico;
    }

    @Override
    public List<Topico> listarPorPreparacao(Long preparacaoId) {
        List<Topico> resultado = new ArrayList<>();
        for (Topico topico : topicos) {
            if (topico.getPreparacaoId().equals(preparacaoId)) {
                resultado.add(topico);
            }
        }
        return resultado;
    }
}