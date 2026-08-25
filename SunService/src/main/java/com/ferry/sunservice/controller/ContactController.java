package com.ferry.sunservice.controller;

import com.ferry.sunservice.dto.ContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "https://sauerburg.at", "https://fotografie-nina.at"})
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private JavaMailSender mailSender;

    // Liest den Empfänger aus der Konfiguration; Fallback ist Ninas E-Mail
    @Value("${contact.mail.recipient:ferdinand.juraczka@kapdion.com}")
    private String recipientEmail;

    @PostMapping
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactRequest request) {
        logger.info("Empfange Kontaktanfrage von: {}", request.getEmail());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ferry.juraczka@gmail.com"); // SMTP-Absender bei Gmail
            message.setTo(recipientEmail);               // Dynamischer Empfänger
            message.setReplyTo(request.getEmail());      // Direktes Antworten im Mail-Programm ermöglichen
            message.setSubject("Neue Anfrage von " + request.getName());
            message.setText("Inhalt:\n" + request.getMessage() + "\n\nAbsender E-Mail: " + request.getEmail());

            mailSender.send(message);

            logger.info("E-Mail erfolgreich an {} übermittelt.", recipientEmail);
            return ResponseEntity.ok("Gesendet!");
        } catch (Exception e) {
            logger.error("FEHLER beim E-Mail Versand: ", e);
            return ResponseEntity.status(500).body("Serverfehler: " + e.getMessage());
        }
    }
}