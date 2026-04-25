package com.br.barbeariaRest.service;

import com.br.barbeariaRest.model.Agendamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");

    @Async
    public void enviarEmailAgendamento(Agendamento agendamento) {
        try {
            enviarParaCliente(agendamento);
            enviarParaBarbeiro(agendamento);
        } catch (Exception e) {
            log.error("Erro ao enviar email para agendamento id={}: {}", agendamento.getId(), e.getMessage());
        }
    }

    private void enviarParaCliente(Agendamento agendamento) {
        String corpo = String.format(
                "Ola, %s!%n%n" +
                        "Seu agendamento foi confirmado:%n%n" +
                        "  Barbeiro: %s%n" +
                        "  Servico:  %s%n" +
                        "  Data/Hora: %s%n" +
                        "  Valor: R$ %.2f%n%n" +
                        "Ate logo!",
                agendamento.getCliente().getNome(),
                agendamento.getBarbeiro().getNome(),
                agendamento.getServico().getNome(),
                agendamento.getDataHora().format(FORMATTER),
                agendamento.getServico().getPreco());

        enviar(
                agendamento.getCliente().getUsuario().getUsername(),
                "Agendamento confirmado - Barbearia",
                corpo);
    }

    private void enviarParaBarbeiro(Agendamento agendamento) {
        String corpo = String.format(
                "Novo agendamento recebido!%n%n" +
                        "  Cliente:   %s%n" +
                        "  Servico:   %s%n" +
                        "  Data/Hora: %s%n" +
                        "  Observacoes: %s",
                agendamento.getCliente().getNome(),
                agendamento.getServico().getNome(),
                agendamento.getDataHora().format(FORMATTER));

        enviar(
                agendamento.getBarbeiro().getUsuario().getUsername(),
                "Novo agendamento - " + agendamento.getCliente().getNome(),
                corpo);
    }

    private void enviar(String para, String assunto, String corpo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(para);
        mensagem.setSubject(assunto);
        mensagem.setText(corpo);
        mailSender.send(mensagem);
        log.info("Email enviado para: {}", para);
    }
}

