package br.rildo.studyflow.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.rildo.studyflow.domain.DesempenhoTopico;
import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.domain.Revisao;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.domain.enums.StatusTopico;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class TopicoWebController {

    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final DisciplinaRepositoryJpa disciplinaRepository;
    private final TopicoRepositoryJpa topicoRepository;
    private final DesempenhoRepositoryJpa desempenhoRepository;
    private final RevisaoRepositoryJpa revisaoRepository;

    public TopicoWebController(
            PreparacaoRepositoryJpa preparacaoRepository,
            DisciplinaRepositoryJpa disciplinaRepository,
            TopicoRepositoryJpa topicoRepository,
            DesempenhoRepositoryJpa desempenhoRepository,
            RevisaoRepositoryJpa revisaoRepository
    ) {
        this.preparacaoRepository = preparacaoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.topicoRepository = topicoRepository;
        this.desempenhoRepository = desempenhoRepository;
        this.revisaoRepository = revisaoRepository;
    }

    @GetMapping("/disciplinas/{disciplinaId}/topicos/novo")
    public String mostrarFormulario(
            @PathVariable Long disciplinaId,
            Model model
    ) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: " + disciplinaId
                ));

        Preparacao preparacao = preparacaoRepository
                .findById(disciplina.getPreparacaoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: "
                                + disciplina.getPreparacaoId()
                ));

        Topico topico = new Topico();
        topico.setPreparacaoId(preparacao.getId());
        topico.setDisciplinaId(disciplina.getId());
        topico.setStatus(StatusTopico.NAO_INICIADO);

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("topico", topico);
        model.addAttribute("statusTopico", StatusTopico.values());
        model.addAttribute("modoEdicao", false);

        return "topico-form";
    }

    @PostMapping("/disciplinas/{disciplinaId}/topicos")
    public String salvar(
            @PathVariable Long disciplinaId,
            @ModelAttribute Topico topico
    ) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: " + disciplinaId
                ));

        topico.setDisciplinaId(disciplina.getId());
        topico.setPreparacaoId(disciplina.getPreparacaoId());

        if (topico.getStatus() == null) {
            topico.setStatus(StatusTopico.NAO_INICIADO);
        }

        if (topico.getCriadoEm() == null) {
            topico.setCriadoEm(LocalDateTime.now());
        }

        topicoRepository.save(topico);

        return "redirect:/preparacoes/" + disciplina.getPreparacaoId();
    }

    @GetMapping("/topicos/{id}/editar")
    public String mostrarFormularioEdicao(
            @PathVariable Long id,
            Model model
    ) {
        Topico topico = buscarTopico(id);
        Disciplina disciplina = buscarDisciplina(topico);
        Preparacao preparacao = buscarPreparacao(topico);

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("topico", topico);
        model.addAttribute("statusTopico", StatusTopico.values());
        model.addAttribute("modoEdicao", true);

        return "topico-form";
    }

    @PostMapping("/topicos/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Topico topicoForm
    ) {
        Topico topicoExistente = buscarTopico(id);

        topicoExistente.setNome(topicoForm.getNome());
        topicoExistente.setPeso(topicoForm.getPeso());
        topicoExistente.setStatus(topicoForm.getStatus());

        topicoRepository.save(topicoExistente);

        return "redirect:/preparacoes/"
                + topicoExistente.getPreparacaoId();
    }

    @GetMapping("/topicos/{id}/excluir")
    public String mostrarConfirmacaoExclusao(
            @PathVariable Long id,
            Model model
    ) {
        Topico topico = buscarTopico(id);
        Disciplina disciplina = buscarDisciplina(topico);
        Preparacao preparacao = buscarPreparacao(topico);

        DesempenhoTopico desempenho = desempenhoRepository
                .findByTopicoId(topico.getId())
                .orElse(null);

        List<Revisao> revisoes =
                revisaoRepository.findByTopicoId(topico.getId());

        boolean possuiDesempenho = desempenho != null;
        boolean possuiRevisoes = !revisoes.isEmpty();
        boolean podeExcluir = !possuiDesempenho && !possuiRevisoes;

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("topico", topico);
        model.addAttribute("possuiDesempenho", possuiDesempenho);
        model.addAttribute("quantidadeRevisoes", revisoes.size());
        model.addAttribute("podeExcluir", podeExcluir);

        return "topico-excluir";
    }

    @PostMapping("/topicos/{id}/excluir")
    public String excluir(
            @PathVariable Long id
    ) {
        Topico topico = buscarTopico(id);

        boolean possuiDesempenho = desempenhoRepository
                .findByTopicoId(topico.getId())
                .isPresent();

        boolean possuiRevisoes = !revisaoRepository
                .findByTopicoId(topico.getId())
                .isEmpty();

        if (possuiDesempenho || possuiRevisoes) {
            return "redirect:/topicos/" + id + "/excluir";
        }

        Long preparacaoId = topico.getPreparacaoId();

        topicoRepository.deleteById(id);

        return "redirect:/preparacoes/" + preparacaoId;
    }

    private Topico buscarTopico(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tópico não encontrado: " + id
                ));
    }

    private Disciplina buscarDisciplina(Topico topico) {
        return disciplinaRepository.findById(topico.getDisciplinaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: "
                                + topico.getDisciplinaId()
                ));
    }

    private Preparacao buscarPreparacao(Topico topico) {
        return preparacaoRepository.findById(topico.getPreparacaoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: "
                                + topico.getPreparacaoId()
                ));
    }
}