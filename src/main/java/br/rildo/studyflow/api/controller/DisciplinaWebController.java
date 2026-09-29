package br.rildo.studyflow.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class DisciplinaWebController {

    private final DisciplinaRepositoryJpa disciplinaRepository;
    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final TopicoRepositoryJpa topicoRepository;

    public DisciplinaWebController(
            DisciplinaRepositoryJpa disciplinaRepository,
            PreparacaoRepositoryJpa preparacaoRepository,
            TopicoRepositoryJpa topicoRepository
    ) {
        this.disciplinaRepository = disciplinaRepository;
        this.preparacaoRepository = preparacaoRepository;
        this.topicoRepository = topicoRepository;
    }

    @GetMapping("/preparacoes/{preparacaoId}/disciplinas/nova")
    public String mostrarFormulario(
            @PathVariable Long preparacaoId,
            Model model
    ) {
        Preparacao preparacao = preparacaoRepository.findById(preparacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + preparacaoId
                ));

        Disciplina disciplina = new Disciplina();
        disciplina.setPreparacaoId(preparacaoId);

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("modoEdicao", false);

        return "disciplina-form";
    }

    @PostMapping("/preparacoes/{preparacaoId}/disciplinas")
    public String salvar(
            @PathVariable Long preparacaoId,
            @ModelAttribute Disciplina disciplina
    ) {
        preparacaoRepository.findById(preparacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + preparacaoId
                ));

        disciplina.setPreparacaoId(preparacaoId);

        if (disciplina.getCriadaEm() == null) {
            disciplina.setCriadaEm(LocalDateTime.now());
        }

        disciplinaRepository.save(disciplina);

        return "redirect:/preparacoes/" + preparacaoId;
    }

    @GetMapping("/disciplinas/{id}/editar")
    public String mostrarFormularioEdicao(
            @PathVariable Long id,
            Model model
    ) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: " + id
                ));

        Preparacao preparacao = preparacaoRepository
                .findById(disciplina.getPreparacaoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: "
                                + disciplina.getPreparacaoId()
                ));

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("modoEdicao", true);

        return "disciplina-form";
    }

    @PostMapping("/disciplinas/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Disciplina disciplinaForm
    ) {
        Disciplina disciplinaExistente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: " + id
                ));

        disciplinaExistente.setNome(disciplinaForm.getNome());
        disciplinaExistente.setPeso(disciplinaForm.getPeso());

        disciplinaRepository.save(disciplinaExistente);

        return "redirect:/preparacoes/"
                + disciplinaExistente.getPreparacaoId();
    }

    @GetMapping("/disciplinas/{id}/excluir")
    public String mostrarConfirmacaoExclusao(
            @PathVariable Long id,
            Model model
    ) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: " + id
                ));

        Preparacao preparacao = preparacaoRepository
                .findById(disciplina.getPreparacaoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: "
                                + disciplina.getPreparacaoId()
                ));

        List<Topico> topicos =
                topicoRepository.findByDisciplinaId(disciplina.getId());

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("quantidadeTopicos", topicos.size());
        model.addAttribute(
                "podeExcluir",
                topicos.isEmpty()
        );

        return "disciplina-excluir";
    }

    @PostMapping("/disciplinas/{id}/excluir")
    public String excluir(
            @PathVariable Long id
    ) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Disciplina não encontrada: " + id
                ));

        List<Topico> topicos =
                topicoRepository.findByDisciplinaId(disciplina.getId());

        if (!topicos.isEmpty()) {
            return "redirect:/disciplinas/" + id + "/excluir";
        }

        Long preparacaoId = disciplina.getPreparacaoId();

        disciplinaRepository.deleteById(id);

        return "redirect:/preparacoes/" + preparacaoId;
    }
}