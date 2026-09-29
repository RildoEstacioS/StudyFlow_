package br.rildo.studyflow.api.controller;

import java.time.LocalDate;
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

import br.rildo.studyflow.domain.DesempenhoTopico;
import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class PreparacaoWebController {

    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final DisciplinaRepositoryJpa disciplinaRepository;
    private final TopicoRepositoryJpa topicoRepository;
    private final DesempenhoRepositoryJpa desempenhoRepository;

    public PreparacaoWebController(
            PreparacaoRepositoryJpa preparacaoRepository,
            DisciplinaRepositoryJpa disciplinaRepository,
            TopicoRepositoryJpa topicoRepository,
            DesempenhoRepositoryJpa desempenhoRepository
    ) {
        this.preparacaoRepository = preparacaoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.topicoRepository = topicoRepository;
        this.desempenhoRepository = desempenhoRepository;
    }

    @GetMapping("/preparacoes")
    public String listar(Model model) {
        List<Preparacao> preparacoes = preparacaoRepository.findAll();
        
        // Ordenar por data (mais próxima primeiro)
        preparacoes.sort((p1, p2) -> p1.getDataAlvo().compareTo(p2.getDataAlvo()));
        
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
    public String salvar(
            @ModelAttribute Preparacao preparacao,
            RedirectAttributes redirectAttributes
    ) {
        // Validação de data
        if (preparacao.getDataAlvo() == null) {
            redirectAttributes.addFlashAttribute("erro", "Data alvo é obrigatória");
            return "redirect:/preparacoes/novo";
        }

        if (preparacao.getDataAlvo().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("erro", "Data alvo não pode ser no passado");
            return "redirect:/preparacoes/novo";
        }

        // Validação de minutos
        if (preparacao.getMinutosDisponiveisPorSemana() == null ||
            preparacao.getMinutosDisponiveisPorSemana() < 60 ||
            preparacao.getMinutosDisponiveisPorSemana() > 5040) {
            redirectAttributes.addFlashAttribute("erro",
                "Minutos semanais devem ser entre 60 e 5040 (84 horas)");
            return "redirect:/preparacoes/novo";
        }

        if (preparacao.getCriadaEm() == null) {
            preparacao.setCriadaEm(LocalDateTime.now());
        }

        preparacaoRepository.save(preparacao);
        redirectAttributes.addFlashAttribute("sucesso", "Preparação criada com sucesso!");
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

        // Buscar todos os desempenhos e popular o Map
        Map<Long, DesempenhoTopico> desempenhosPorTopico = new HashMap<>();
        for (Disciplina disciplina : disciplinas) {
            List<Topico> topicos = topicoRepository.findByDisciplinaId(disciplina.getId());
            if (topicos != null) {
                for (Topico topico : topicos) {
                    try {
                        DesempenhoTopico desempenho = desempenhoRepository
                                .findByTopicoId(topico.getId())
                                .orElse(null);
                        if (desempenho != null) {
                            desempenhosPorTopico.put(topico.getId(), desempenho);
                        }
                    } catch (Exception e) {
                        // Se tiver múltiplos, ignora este tópico
                    }
                }
            }
        }
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
            @ModelAttribute Preparacao preparacaoForm,
            RedirectAttributes redirectAttributes
    ) {
        Preparacao preparacaoExistente = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        // Validação de data
        if (preparacaoForm.getDataAlvo() == null) {
            redirectAttributes.addFlashAttribute("erro", "Data alvo é obrigatória");
            return "redirect:/preparacoes/" + id + "/editar";
        }

        if (preparacaoForm.getDataAlvo().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("erro", "Data alvo não pode ser no passado");
            return "redirect:/preparacoes/" + id + "/editar";
        }

        // Validação de minutos
        if (preparacaoForm.getMinutosDisponiveisPorSemana() == null ||
            preparacaoForm.getMinutosDisponiveisPorSemana() < 60 ||
            preparacaoForm.getMinutosDisponiveisPorSemana() > 5040) {
            redirectAttributes.addFlashAttribute("erro",
                "Minutos semanais devem ser entre 60 e 5040 (84 horas)");
            return "redirect:/preparacoes/" + id + "/editar";
        }

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
        redirectAttributes.addFlashAttribute("sucesso", "Preparação atualizada com sucesso!");

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
        // Verificar se tem disciplinas vinculadas
        List<Disciplina> disciplinas = disciplinaRepository.findByPreparacaoId(id);
        if (disciplinas != null && !disciplinas.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro",
                "Não é possível excluir preparação com disciplinas vinculadas. " +
                "Exclua as disciplinas primeiro.");
            return "redirect:/preparacoes";
        }

        try {
            preparacaoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("sucesso", "Preparação excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Não foi possível excluir esta preparação.");
        }
        return "redirect:/preparacoes";
    }
}