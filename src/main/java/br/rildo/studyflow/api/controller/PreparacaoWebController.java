package br.rildo.studyflow.api.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class PreparacaoWebController {

    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final DisciplinaRepositoryJpa disciplinaRepository;
    private final TopicoRepositoryJpa topicoRepository;

    public PreparacaoWebController(
            PreparacaoRepositoryJpa preparacaoRepository,
            DisciplinaRepositoryJpa disciplinaRepository,
            TopicoRepositoryJpa topicoRepository
    ) {
        this.preparacaoRepository = preparacaoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.topicoRepository = topicoRepository;
    }

    @GetMapping("/preparacoes")
    public String listar(Model model) {
        List<Preparacao> preparacoes = preparacaoRepository.findAll();
        model.addAttribute("preparacoes", preparacoes);
        return "preparacoes";
    }

    @GetMapping("/preparacoes/novo")
    public String mostrarFormulario(Model model) {
        Preparacao preparacao = new Preparacao();
        model.addAttribute("preparacao", preparacao);
        model.addAttribute("modoEdicao", false);
        return "preparacao-form";
    }

    @PostMapping("/preparacoes")
    public String salvar(@ModelAttribute Preparacao preparacao) {
        if (preparacao.getCriadaEm() == null) {
            preparacao.setCriadaEm(LocalDateTime.now());
        }
        preparacaoRepository.save(preparacao);
        return "redirect:/preparacoes";
    }

        @GetMapping("/preparacoes/{id}")
        public String detalhe(
                @PathVariable Long id,
                Model model
        ) {
        Preparacao preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        model.addAttribute("preparacao", preparacao);

        List<Disciplina> disciplinas = disciplinaRepository.findByPreparacaoId(id);
        model.addAttribute("disciplinas", disciplinas != null ? disciplinas : new ArrayList<>());

        Map<Long, List<Topico>> topicosPorDisciplina = new HashMap<>();
        for (Disciplina disciplina : disciplinas) {
                List<Topico> topicos = topicoRepository.findByDisciplinaId(disciplina.getId());
                topicosPorDisciplina.put(disciplina.getId(), topicos != null ? topicos : new ArrayList<>());
        }
        model.addAttribute("topicosPorDisciplina", topicosPorDisciplina);

        Map<Long, Object> desempenhosPorTopico = new HashMap<>();
        model.addAttribute("desempenhosPorTopico", desempenhosPorTopico);

        return "preparacao-detalhe";
        }

    @GetMapping("/preparacoes/{id}/editar")
    public String mostrarFormularioEdicao(
            @PathVariable Long id,
            Model model
    ) {
        Preparacao preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("modoEdicao", true);
        return "preparacao-form";
    }

    @PostMapping("/preparacoes/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Preparacao preparacaoForm
    ) {
        Preparacao preparacaoExistente = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        preparacaoExistente.setNome(preparacaoForm.getNome());
        preparacaoExistente.setBanca(preparacaoForm.getBanca());
        preparacaoExistente.setDataAlvo(preparacaoForm.getDataAlvo());
        preparacaoExistente.setTipoDataAlvo(preparacaoForm.getTipoDataAlvo());
        preparacaoExistente.setMinutosDisponiveisPorSemana(
                preparacaoForm.getMinutosDisponiveisPorSemana()
        );
        preparacaoExistente.setEstrategiaPlanejamento(
                preparacaoForm.getEstrategiaPlanejamento()
        );

        preparacaoRepository.save(preparacaoExistente);

        return "redirect:/preparacoes/" + id;
    }

    @GetMapping("/preparacoes/{id}/excluir")
    public String mostrarConfirmacaoExclusao(
            @PathVariable Long id,
            Model model
    ) {
        Preparacao preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        model.addAttribute("preparacao", preparacao);
        return "preparacao-excluir";
    }

    @PostMapping("/preparacoes/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            preparacaoRepository.deleteById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Não foi possível excluir esta preparação.");
            return "redirect:/preparacoes";
        }
        return "redirect:/preparacoes";
    }
}