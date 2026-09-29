package br.rildo.studyflow.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.rildo.studyflow.application.service.DesempenhoService;
import br.rildo.studyflow.application.service.RevisaoService;
import br.rildo.studyflow.domain.DesempenhoTopico;
import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.domain.Topico;
import br.rildo.studyflow.infrastructure.repository.DesempenhoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.RevisaoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.SessaoEstudoRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.TopicoRepositoryJpa;

@Controller
public class DesempenhoWebController {

    private final DesempenhoService desempenhoService;
    private final TopicoRepositoryJpa topicoRepository;
    private final DisciplinaRepositoryJpa disciplinaRepository;
    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final DesempenhoRepositoryJpa desempenhoRepository;

    public DesempenhoWebController(
            TopicoRepositoryJpa topicoRepository,
            DisciplinaRepositoryJpa disciplinaRepository,
            PreparacaoRepositoryJpa preparacaoRepository,
            DesempenhoRepositoryJpa desempenhoRepository,
            RevisaoRepositoryJpa revisaoRepository,
            SessaoEstudoRepositoryJpa repositorioSessao
    ) {
        this.topicoRepository = topicoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.preparacaoRepository = preparacaoRepository;
        this.desempenhoRepository = desempenhoRepository;

        RevisaoService revisaoService = new RevisaoService(revisaoRepository);
        this.desempenhoService = new DesempenhoService(
            desempenhoRepository,
            repositorioSessao,
            revisaoService
        );
    }

    @GetMapping("/topicos/{id}/desempenho")
    public String mostrarFormularioDesempenho(
            @PathVariable Long id,
            Model model
    ) {
        Topico topico = buscarTopico(id);
        Disciplina disciplina = buscarDisciplina(topico);
        Preparacao preparacao = buscarPreparacao(topico);

        DesempenhoTopico desempenho = null;
        try {
            desempenho = desempenhoRepository
                    .findByTopicoId(topico.getId())
                    .orElse(null);
        } catch (Exception e) {
            // Se tiver múltiplos registros, ignora e usa null
            // O formulário vai mostrar 0, 0
        }

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("topico", topico);

        DesempenhoForm form = new DesempenhoForm();
        if (desempenho != null) {
            form.setQuestoesRespondidas(desempenho.getQuestoesRespondidas());
            form.setAcertos(desempenho.getAcertos());
        } else {
            form.setQuestoesRespondidas(0);
            form.setAcertos(0);
        }
        model.addAttribute("desempenhoForm", form);

        return "desempenho-form";
    }

    @PostMapping("/topicos/{id}/desempenho")
    public String salvarDesempenho(
            @PathVariable Long id,
            @ModelAttribute DesempenhoForm form
    ) {
        Topico topico = buscarTopico(id);

        desempenhoService.registrarSessao(
            topico.getId(),
            form.getQuestoesRespondidas(),
            form.getAcertos()
        );

        return "redirect:/preparacoes/" + topico.getPreparacaoId();
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

    /**
     * DTO simples para o formulário web de desempenho.
     */
    public static class DesempenhoForm {
        private int questoesRespondidas;
        private int acertos;

        public DesempenhoForm() {
        }

        public int getQuestoesRespondidas() {
            return questoesRespondidas;
        }

        public void setQuestoesRespondidas(int questoesRespondidas) {
            this.questoesRespondidas = questoesRespondidas;
        }

        public int getAcertos() {
            return acertos;
        }

        public void setAcertos(int acertos) {
            this.acertos = acertos;
        }
    }
}