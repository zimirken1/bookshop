package com.scand.bookshop.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

  @Value("${email.from}")
  private String fromEmail;

  @Value("${email.password}")
  private String emailPassword;

  @Value("${smtp.server}")
  private String smtpServer;

  @Value("${smtp.port}")
  private int smtpPort;

  public void sendEmail(String to, String subject, String body) {
    Email email = EmailBuilder.startingBlank()
        .from(fromEmail)
        .to(to)
        .withSubject(subject)
        .withPlainText(body)
        .buildEmail();

    Mailer mailer = MailerBuilder
        .withTransportStrategy(TransportStrategy.SMTPS)
        .withSMTPServer(smtpServer, smtpPort, fromEmail, emailPassword)
        .buildMailer();

    mailer.sendMail(email);
  }
}