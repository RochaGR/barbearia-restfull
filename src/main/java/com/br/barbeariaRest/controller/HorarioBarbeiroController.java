package com.br.barbeariaRest.controller;

import com.br.barbeariaRest.dto.request.ExcecaoHorarioRequestDTO;
import com.br.barbeariaRest.dto.request.HorarioTrabalhoRequestDTO;
import com.br.barbeariaRest.exception.EntityNotFoundException;
import com.br.barbeariaRest.exception.ValidationException;
import com.br.barbeariaRest.model.Barbeiro;
import com.br.barbeariaRest.model.ExcecaoHorarioBarbeiro;
import com.br.barbeariaRest.model.HorarioTrabalhoBarbeiro;
import com.br.barbeariaRest.repository.BarbeiroRepository;
import com.br.barbeariaRest.repository.ExcecaoHorarioBarbeiroRepository;
import com.br.barbeariaRest.repository.HorarioTrabalhoBarbeiroRepository;
import com.br.barbeariaRest.service.DisponibilidadeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/barbeiros/{barbeiroId}/horarios")
@Slf4j
public class HorarioBarbeiroController {

    @Autowired
    private HorarioTrabalhoBarbeiroRepository horarioRepository;

    @Autowired
    private ExcecaoHorarioBarbeiroRepository excecaoRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    // === HORÁRIOS DE TRABALHO ===

    @GetMapping
    public ResponseEntity<List<HorarioTrabalhoBarbeiro>> getHorarios(@PathVariable Long barbeiroId) {
        log.info("Consultando horários do barbeiro {}", barbeiroId);

        // Verificar se barbeiro existe
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new EntityNotFoundException("Barbeiro não encontrado");
        }

        List<HorarioTrabalhoBarbeiro> horarios = horarioRepository.findByBarbeiroIdAndAtivoTrue(barbeiroId);
        return ResponseEntity.ok(horarios);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HorarioTrabalhoBarbeiro> criarHorario(
            @PathVariable Long barbeiroId,
            @Valid @RequestBody HorarioTrabalhoRequestDTO request) {

        log.info("Criando horário para barbeiro {} no dia {}", barbeiroId, request.getDiaSemana());

        // Verificar se barbeiro existe e está ativo
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro não encontrado"));

        if (!barbeiro.isAtivo()) {
            throw new ValidationException("Não é possível definir horários para barbeiro inativo");
        }

        // Validar horários
        validarHorarios(request.getHoraInicio(), request.getHoraFim(),
                request.getPausaInicio(), request.getPausaFim());

        // Desativar horário existente para o mesmo dia
        horarioRepository.findByBarbeiroIdAndDiaSemanaAndAtivoTrue(barbeiroId, request.getDiaSemana())
                .ifPresent(existente -> {
                    log.info("Desativando horário existente para barbeiro {} no dia {}",
                            barbeiroId, request.getDiaSemana());
                    existente.setAtivo(false);
                    horarioRepository.save(existente);
                });

        // Criar novo horário
        HorarioTrabalhoBarbeiro horario = new HorarioTrabalhoBarbeiro();
        horario.setBarbeiro(barbeiro);
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFim(request.getHoraFim());
        horario.setPausaInicio(request.getPausaInicio());
        horario.setPausaFim(request.getPausaFim());
        horario.setAtivo(true);

        HorarioTrabalhoBarbeiro salvo = horarioRepository.save(horario);
        log.info("Horário criado com sucesso: {}", salvo.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{horarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HorarioTrabalhoBarbeiro> atualizarHorario(
            @PathVariable Long barbeiroId,
            @PathVariable Long horarioId,
            @Valid @RequestBody HorarioTrabalhoRequestDTO request) {

        log.info("Atualizando horário {} do barbeiro {}", horarioId, barbeiroId);

        HorarioTrabalhoBarbeiro existente = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado"));

        if (!existente.getBarbeiro().getId().equals(barbeiroId)) {
            throw new ValidationException("Horário não pertence ao barbeiro informado");
        }

        // Validar horários
        validarHorarios(request.getHoraInicio(), request.getHoraFim(),
                request.getPausaInicio(), request.getPausaFim());

        existente.setDiaSemana(request.getDiaSemana());
        existente.setHoraInicio(request.getHoraInicio());
        existente.setHoraFim(request.getHoraFim());
        existente.setPausaInicio(request.getPausaInicio());
        existente.setPausaFim(request.getPausaFim());

        HorarioTrabalhoBarbeiro atualizado = horarioRepository.save(existente);
        log.info("Horário atualizado com sucesso: {}", atualizado.getId());

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{horarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarHorario(@PathVariable Long barbeiroId, @PathVariable Long horarioId) {
        log.info("Deletando horário {} do barbeiro {}", horarioId, barbeiroId);

        HorarioTrabalhoBarbeiro horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado"));

        if (!horario.getBarbeiro().getId().equals(barbeiroId)) {
            throw new ValidationException("Horário não pertence ao barbeiro informado");
        }

        horario.setAtivo(false);
        horarioRepository.save(horario);
        log.info("Horário desativado com sucesso: {}", horarioId);

        return ResponseEntity.noContent().build();
    }

    // === EXCEÇÕES (Folgas, Férias, etc.) ===

    @GetMapping("/excecoes")
    public ResponseEntity<List<ExcecaoHorarioBarbeiro>> getExcecoes(
            @PathVariable Long barbeiroId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        log.info("Consultando exceções do barbeiro {}", barbeiroId);

        // Verificar se barbeiro existe
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new EntityNotFoundException("Barbeiro não encontrado");
        }

        // Definir período padrão se não informado
        if (dataInicio == null) {
            dataInicio = LocalDate.now().minusMonths(1);
        }
        if (dataFim == null) {
            dataFim = LocalDate.now().plusMonths(3);
        }

        // CORREÇÃO: Parâmetros na ordem correta
        List<ExcecaoHorarioBarbeiro> excecoes = excecaoRepository
                .findByBarbeiroIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqualAndAtivoTrue(
                        barbeiroId, dataFim, dataInicio);

        return ResponseEntity.ok(excecoes);
    }

    @PostMapping("/excecoes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExcecaoHorarioBarbeiro> criarExcecao(
            @PathVariable Long barbeiroId,
            @Valid @RequestBody ExcecaoHorarioRequestDTO request) {

        log.info("Criando exceção para barbeiro {} do tipo {}", barbeiroId, request.getTipo());

        // Verificar se barbeiro existe
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro não encontrado"));

        // Validar datas
        if (request.getDataInicio().isAfter(request.getDataFim())) {
            throw new ValidationException("Data de início deve ser anterior ou igual à data de fim");
        }

        // Validar horários se informados
        if (request.getHoraInicio() != null && request.getHoraFim() != null) {
            if (request.getHoraInicio().isAfter(request.getHoraFim())) {
                throw new ValidationException("Hora de início deve ser anterior à hora de fim");
            }
        }

        // Criar exceção
        ExcecaoHorarioBarbeiro excecao = new ExcecaoHorarioBarbeiro();
        excecao.setBarbeiro(barbeiro);
        excecao.setDataInicio(request.getDataInicio());
        excecao.setDataFim(request.getDataFim());
        excecao.setHoraInicio(request.getHoraInicio());
        excecao.setHoraFim(request.getHoraFim());
        excecao.setTipo(request.getTipo());
        excecao.setObservacao(request.getObservacao());
        excecao.setAtivo(true);

        ExcecaoHorarioBarbeiro salva = excecaoRepository.save(excecao);
        log.info("Exceção criada com sucesso: {}", salva.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/excecoes/{excecaoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExcecaoHorarioBarbeiro> atualizarExcecao(
            @PathVariable Long barbeiroId,
            @PathVariable Long excecaoId,
            @Valid @RequestBody ExcecaoHorarioRequestDTO request) {

        log.info("Atualizando exceção {} do barbeiro {}", excecaoId, barbeiroId);

        ExcecaoHorarioBarbeiro existente = excecaoRepository.findById(excecaoId)
                .orElseThrow(() -> new EntityNotFoundException("Exceção não encontrada"));

        if (!existente.getBarbeiro().getId().equals(barbeiroId)) {
            throw new ValidationException("Exceção não pertence ao barbeiro informado");
        }

        // Validar datas
        if (request.getDataInicio().isAfter(request.getDataFim())) {
            throw new ValidationException("Data de início deve ser anterior ou igual à data de fim");
        }

        // Validar horários se informados
        if (request.getHoraInicio() != null && request.getHoraFim() != null) {
            if (request.getHoraInicio().isAfter(request.getHoraFim())) {
                throw new ValidationException("Hora de início deve ser anterior à hora de fim");
            }
        }

        existente.setDataInicio(request.getDataInicio());
        existente.setDataFim(request.getDataFim());
        existente.setHoraInicio(request.getHoraInicio());
        existente.setHoraFim(request.getHoraFim());
        existente.setTipo(request.getTipo());
        existente.setObservacao(request.getObservacao());

        ExcecaoHorarioBarbeiro atualizada = excecaoRepository.save(existente);
        log.info("Exceção atualizada com sucesso: {}", atualizada.getId());

        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/excecoes/{excecaoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarExcecao(@PathVariable Long barbeiroId, @PathVariable Long excecaoId) {
        log.info("Deletando exceção {} do barbeiro {}", excecaoId, barbeiroId);

        ExcecaoHorarioBarbeiro excecao = excecaoRepository.findById(excecaoId)
                .orElseThrow(() -> new EntityNotFoundException("Exceção não encontrada"));

        if (!excecao.getBarbeiro().getId().equals(barbeiroId)) {
            throw new ValidationException("Exceção não pertence ao barbeiro informado");
        }

        excecao.setAtivo(false);
        excecaoRepository.save(excecao);
        log.info("Exceção desativada com sucesso: {}", excecaoId);

        return ResponseEntity.noContent().build();
    }

    // === CONSULTAS DE DISPONIBILIDADE ===

    @GetMapping("/disponiveis")
    public ResponseEntity<List<LocalTime>> getHorariosDisponiveis(
            @PathVariable Long barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        log.info("Consultando horários disponíveis do barbeiro {} na data {}", barbeiroId, data);

        // Verificar se barbeiro existe
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new EntityNotFoundException("Barbeiro não encontrado");
        }

        // Validar se a data não é no passado
        if (data.isBefore(LocalDate.now())) {
            throw new ValidationException("Não é possível consultar horários em datas passadas");
        }

        List<LocalTime> horarios = disponibilidadeService.getHorariosDisponiveis(barbeiroId, data);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/disponiveis/periodo")
    public ResponseEntity<Map<LocalDate, List<LocalTime>>> getDisponibilidadePeriodo(
            @PathVariable Long barbeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        log.info("Consultando disponibilidade do barbeiro {} no período {} a {}",
                barbeiroId, dataInicio, dataFim);

        // Verificar se barbeiro existe
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new EntityNotFoundException("Barbeiro não encontrado");
        }

        // Validações
        if (dataInicio.isAfter(dataFim)) {
            throw new ValidationException("Data de início deve ser anterior ou igual à data de fim");
        }

        if (dataInicio.isBefore(LocalDate.now())) {
            throw new ValidationException("Data de início não pode ser no passado");
        }

        // Limitar consulta a 30 dias para evitar sobrecarga
        if (dataInicio.plusDays(30).isBefore(dataFim)) {
            throw new ValidationException("Período máximo de consulta é 30 dias");
        }

        Map<LocalDate, List<LocalTime>> disponibilidade = new HashMap<>();

        LocalDate dataAtual = dataInicio;
        while (!dataAtual.isAfter(dataFim)) {
            List<LocalTime> horariosDisponiveis = disponibilidadeService
                    .getHorariosDisponiveis(barbeiroId, dataAtual);

            if (!horariosDisponiveis.isEmpty()) {
                disponibilidade.put(dataAtual, horariosDisponiveis);
            }

            dataAtual = dataAtual.plusDays(1);
        }

        return ResponseEntity.ok(disponibilidade);
    }

    // === MÉTODOS AUXILIARES ===

    private void validarHorarios(LocalTime horaInicio, LocalTime horaFim,
                                 LocalTime pausaInicio, LocalTime pausaFim) {

        // Validar horário de trabalho
        if (horaInicio.isAfter(horaFim)) {
            throw new ValidationException("Hora de início deve ser anterior à hora de fim");
        }

        // Validar horário de pausa se informado
        if (pausaInicio != null && pausaFim != null) {
            if (pausaInicio.isAfter(pausaFim)) {
                throw new ValidationException("Hora de início da pausa deve ser anterior à hora de fim da pausa");
            }

            // Pausa deve estar dentro do horário de trabalho
            if (pausaInicio.isBefore(horaInicio) || pausaFim.isAfter(horaFim)) {
                throw new ValidationException("Horário de pausa deve estar dentro do horário de trabalho");
            }
        }

        // Se só um horário de pausa foi informado
        if ((pausaInicio != null && pausaFim == null) || (pausaInicio == null && pausaFim != null)) {
            throw new ValidationException("Ambos os horários de pausa devem ser informados ou nenhum dos dois");
        }
    }
}