package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.*;
import com.br.barbeariaRest.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class DisponibilidadeService {

    @Autowired
    private ConfiguracaoBarbeariaRepository configRepository;

    @Autowired
    private HorarioTrabalhoBarbeiroRepository horarioRepository;

    @Autowired
    private ExcecaoHorarioBarbeiroRepository excecaoRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    /**
     * Verifica se um horário está disponível para agendamento
     */
    public boolean isHorarioDisponivel(Long barbeiroId, LocalDateTime dataHora, Integer duracaoMinutos) {
        LocalDateTime fimAgendamento = dataHora.plusMinutes(duracaoMinutos);

        // 1. Verificar se a barbearia funciona neste horário
        if (!isBarbeariaFuncionando(dataHora)) {
            log.debug("Barbearia não funciona no horário solicitado: {}", dataHora);
            return false;
        }

        // 2. Verificar se o barbeiro trabalha neste dia/horário
        if (!isBarbeiroDisponivel(barbeiroId, dataHora, fimAgendamento)) {
            log.debug("Barbeiro {} não disponível no horário: {}", barbeiroId, dataHora);
            return false;
        }

        // 3. Verificar conflitos com outros agendamentos
        if (hasConflitoAgendamento(barbeiroId, dataHora, fimAgendamento, null)) {
            log.debug("Conflito de agendamento para barbeiro {} no horário: {}", barbeiroId, dataHora);
            return false;
        }

        // 4. Verificar antecedência mínima
        if (!isAntecedenciaMinimaAtendida(dataHora)) {
            log.debug("Antecedência mínima não atendida para horário: {}", dataHora);
            return false;
        }

        return true;
    }

    /**
     * Verifica se a barbearia funciona no horário solicitado
     */
    private boolean isBarbeariaFuncionando(LocalDateTime dataHora) {
        ConfiguracaoBarbearia config = configRepository.findByAtivoTrue()
                .orElse(getConfiguracaoPadrao());

        // Verificar dia da semana
        int diaSemana = dataHora.getDayOfWeek().getValue(); // 1=Segunda, 7=Domingo
        if (!config.getDiasFuncionamento().contains(diaSemana)) {
            return false;
        }

        // Verificar horário
        LocalTime hora = dataHora.toLocalTime();
        return !hora.isBefore(config.getHoraAbertura()) &&
                !hora.isAfter(config.getHoraFechamento());
    }

    /**
     * Verifica se a antecedência mínima é atendida
     */
    private boolean isAntecedenciaMinimaAtendida(LocalDateTime dataHora) {
        ConfiguracaoBarbearia config = configRepository.findByAtivoTrue()
                .orElse(getConfiguracaoPadrao());

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime minimoPermitido = agora.plusHours(config.getAntecedenciaMinimaHoras());

        return dataHora.isAfter(minimoPermitido);
    }

    /**
     * Verifica disponibilidade específica do barbeiro
     */
    private boolean isBarbeiroDisponivel(Long barbeiroId, LocalDateTime inicio, LocalDateTime fim) {
        LocalDate data = inicio.toLocalDate();
        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFim = fim.toLocalTime();

        // 1. Verificar exceções (folgas, férias, etc.)
        List<ExcecaoHorarioBarbeiro> excecoes = excecaoRepository.findExcecoesPorData(barbeiroId, data);

        for (ExcecaoHorarioBarbeiro excecao : excecoes) {
            switch (excecao.getTipo()) {
                case FOLGA:
                case FERIAS:
                    // Se tem exceção de folga/férias, não está disponível
                    if (isHorarioDentroExcecao(excecao, horaInicio, horaFim)) {
                        return false;
                    }
                    break;

                case HORARIO_ESPECIAL:
                    // Usar horário especial ao invés do padrão
                    return isHorarioDisponivelNoIntervalo(excecao.getHoraInicio(), excecao.getHoraFim(),
                            horaInicio, horaFim);

                case DISPONIVEL:
                    // Disponível mesmo em dia que normalmente não trabalha
                    return isHorarioDisponivelNoIntervalo(excecao.getHoraInicio(), excecao.getHoraFim(),
                            horaInicio, horaFim);
            }
        }

        // 2. Verificar horário normal de trabalho
        int diaSemana = data.getDayOfWeek().getValue();
        Optional<HorarioTrabalhoBarbeiro> horarioTrabalho =
                horarioRepository.findByBarbeiroIdAndDiaSemanaAndAtivoTrue(barbeiroId, diaSemana);

        if (horarioTrabalho.isEmpty()) {
            return false; // Barbeiro não trabalha neste dia
        }

        HorarioTrabalhoBarbeiro horario = horarioTrabalho.get();

        // Verificar se está dentro do horário de trabalho
        if (!isHorarioDisponivelNoIntervalo(horario.getHoraInicio(), horario.getHoraFim(),
                horaInicio, horaFim)) {
            return false;
        }

        // Verificar se não está no horário de pausa
        if (horario.getPausaInicio() != null && horario.getPausaFim() != null) {
            if (isHorarioConflitaComPausa(horario.getPausaInicio(), horario.getPausaFim(),
                    horaInicio, horaFim)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica conflitos com agendamentos existentes
     */
    public boolean hasConflitoAgendamento(Long barbeiroId, LocalDateTime inicio,
                                          LocalDateTime fim, Long agendamentoIdIgnorar) {
        List<Agendamento> conflitos = agendamentoRepository.findConflitosHorario(
                barbeiroId, inicio, fim, agendamentoIdIgnorar);

        return !conflitos.isEmpty();
    }

    /**
     * Gera horários disponíveis para um barbeiro em uma data
     */
    public List<LocalTime> getHorariosDisponiveis(Long barbeiroId, LocalDate data) {
        List<LocalTime> horariosDisponiveis = new ArrayList<>();

        ConfiguracaoBarbearia config = configRepository.findByAtivoTrue()
                .orElse(getConfiguracaoPadrao());

        // Verificar se a barbearia funciona neste dia
        int diaSemana = data.getDayOfWeek().getValue();
        if (!config.getDiasFuncionamento().contains(diaSemana)) {
            return horariosDisponiveis; // Lista vazia
        }

        // Começar do horário de abertura com intervalos configurados
        LocalTime horaAtual = config.getHoraAbertura();
        LocalTime horaFechamento = config.getHoraFechamento();

        while (horaAtual.isBefore(horaFechamento)) {
            LocalDateTime dataHora = LocalDateTime.of(data, horaAtual);

            // Assumir duração padrão de 30 minutos para verificação
            if (isHorarioDisponivel(barbeiroId, dataHora, config.getIntervaloAgendamentoMinutos())) {
                horariosDisponiveis.add(horaAtual);
            }

            horaAtual = horaAtual.plusMinutes(config.getIntervaloAgendamentoMinutos());
        }

        return horariosDisponiveis;
    }

    // Métodos auxiliares
    private boolean isHorarioDentroExcecao(ExcecaoHorarioBarbeiro excecao,
                                           LocalTime horaInicio, LocalTime horaFim) {
        // Se não tem horário específico na exceção, é o dia todo
        if (excecao.getHoraInicio() == null || excecao.getHoraFim() == null) {
            return true;
        }

        // Verificar sobreposição de horários
        return !(horaFim.isBefore(excecao.getHoraInicio()) ||
                horaInicio.isAfter(excecao.getHoraFim()));
    }

    private boolean isHorarioDisponivelNoIntervalo(LocalTime inicioTrabalho, LocalTime fimTrabalho,
                                                   LocalTime inicioSolicitado, LocalTime fimSolicitado) {
        return !inicioSolicitado.isBefore(inicioTrabalho) &&
                !fimSolicitado.isAfter(fimTrabalho);
    }

    private boolean isHorarioConflitaComPausa(LocalTime inicioPausa, LocalTime fimPausa,
                                              LocalTime inicioSolicitado, LocalTime fimSolicitado) {
        return !(fimSolicitado.isBefore(inicioPausa) ||
                inicioSolicitado.isAfter(fimPausa));
    }

    private ConfiguracaoBarbearia getConfiguracaoPadrao() {
        ConfiguracaoBarbearia config = new ConfiguracaoBarbearia();
        config.setHoraAbertura(LocalTime.of(8, 0));
        config.setHoraFechamento(LocalTime.of(18, 0));
        config.setIntervaloAgendamentoMinutos(30);
        config.setAntecedenciaMinimaHoras(2);
        config.setDiasFuncionamento(Set.of(1, 2, 3, 4, 5, 6)); // Seg-Sáb
        return config;
    }
}