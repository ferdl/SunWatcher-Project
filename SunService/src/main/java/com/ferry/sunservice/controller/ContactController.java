package com.ferry.sunservice.controller;

import com.ferry.sunservice.dto.ContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactRequest request) {
        logger.info("Empfange Kontaktanfrage von: {}", request.getEmail());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("deine.gmail.adresse@gmail.com"); // Muss bei Gmail oft mit dem Account übereinstimmen
            message.setTo("nina@fotografie-nina.at");
            message.setSubject("Neue Nachricht von " + request.getName());
            message.setText("Inhalt:\n" + request.getMessage() + "\n\nAntwort an: " + request.getEmail());

            mailSender.send(message);

            logger.info("E-Mail erfolgreich an Gmail übermittelt.");
            return ResponseEntity.ok("Gesendet!");
        } catch (Exception e) {
            logger.error("FEHLER beim E-Mail Versand: ", e);
            return ResponseEntity.status(500).body("Serverfehler: " + e.getMessage());
        }
    }
}