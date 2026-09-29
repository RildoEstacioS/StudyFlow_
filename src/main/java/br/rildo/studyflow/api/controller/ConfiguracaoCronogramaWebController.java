package br.rildo.studyflow.api.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.rildo.studyflow.domain.ConfigDiaSemana;
import br.rildo.studyflow.domain.ConfiguracaoCronograma;
import br.rildo.studyflow.domain.Disciplina;
import br.rildo.studyflow.domain.Preparacao;
import br.rildo.studyflow.domain.enums.DiaSemana;
import br.rildo.studyflow.domain.enums.FrequenciaSimulado;
import br.rildo.studyflow.domain.enums.TipoRevisao;
import br.rildo.studyflow.infrastructure.repository.ConfigDiaSemanaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.ConfiguracaoCronogramaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.DisciplinaRepositoryJpa;
import br.rildo.studyflow.infrastructure.repository.PreparacaoRepositoryJpa;

@Controller
public class ConfiguracaoCronogramaWebController {

    private final ConfiguracaoCronogramaRepositoryJpa repositorioConfig;
    private final ConfigDiaSemanaRepositoryJpa repositorioDia;
    private final PreparacaoRepositoryJpa preparacaoRepository;
    private final DisciplinaRepositoryJpa disciplinaRepository;

    public ConfiguracaoCronogramaWebController(
            ConfiguracaoCronogramaRepositoryJpa repositorioConfig,
            ConfigDiaSemanaRepositoryJpa repositorioDia,
            PreparacaoRepositoryJpa preparacaoRepository,
            DisciplinaRepositoryJpa disciplinaRepository
    ) {
        this.repositorioConfig = repositorioConfig;
        this.repositorioDia = repositorioDia;
        this.preparacaoRepository = preparacaoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/preparacoes/{id}/cronograma/configurar")
    public String mostrarFormulario(
            @PathVariable Long id,
            Model model
    ) {
        Preparacao preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        List<Disciplina> disciplinas =
                disciplinaRepository.findByPreparacaoId(id);

        ConfiguracaoCronograma config = repositorioConfig
                .buscarPorPreparacao(id)
                .orElse(new ConfiguracaoCronograma());

        if (config.getPreparacaoId() == null) {
            config.setPreparacaoId(id);
        }

        List<ConfigDiaSemana> configsDia = repositorioDia.listarPorPreparacao(id);
        configsDia.sort(Comparator.comparingInt(
                c -> c.getDiaSemana().getValor()
        ));

        model.addAttribute("preparacao", preparacao);
        model.addAttribute("disciplinas", disciplinas);
        model.addAttribute("config", config);
        model.addAttribute("configsDia", configsDia);
        model.addAttribute("frequenciasSimulado", FrequenciaSimulado.values());
        model.addAttribute("tiposRevisao", TipoRevisao.values());
        model.addAttribute("diasSemana", DiaSemana.values());

        return "cronograma-config-form";
    }

    @PostMapping("/preparacoes/{id}/cronograma/configurar")
    public String salvar(
            @PathVariable Long id,
            @ModelAttribute ConfiguracaoCronograma config,
            @ModelAttribute List<ConfigDiaSemanaForm> configsDia
    ) {
        Preparacao preparacao = preparacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Preparação não encontrada: " + id
                ));

        config.setPreparacaoId(id);
        repositorioConfig.salvar(config);

        repositorioDia.excluirPorPreparacao(id);

        if (configsDia != null) {
            for (ConfigDiaSemanaForm form : configsDia) {
                if (form.isSelecionado()) {
                    ConfigDiaSemana dia = new ConfigDiaSemana();
                    dia.setPreparacaoId(id);
                    dia.setDiaSemana(DiaSemana.deValor(form.getDiaSemanaValor()));
                    dia.setMinutosDiarios(form.getMinutosDiarios());
                    repositorioDia.salvar(dia);
                }
            }
        }

        return "redirect:/preparacoes/" + id;
    }

    public static class ConfigDiaSemanaForm {
        private int diaSemanaValor;
        private int minutosDiarios;
        private boolean selecionado;

        public ConfigDiaSemanaForm() {
        }

        public int getDiaSemanaValor() {
            return diaSemanaValor;
        }

        public void setDiaSemanaValor(int diaSemanaValor) {
            this.diaSemanaValor = diaSemanaValor;
        }

        public int getMinutosDiarios() {
            return minutosDiarios;
        }

        public void setMinutosDiarios(int minutosDiarios) {
            this.minutosDiarios = minutosDiarios;
        }

        public boolean isSelecionado() {
            return selecionado;
        }

        public void setSelecionado(boolean selecionado) {
            this.selecionado = selecionado;
        }
    }
}