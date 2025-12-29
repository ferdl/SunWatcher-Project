package com.ferry.sunservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class SunServiceApplication {
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 1. Verbindungseinstellungen
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);

        // 2. Zugangsdaten
        // Ersetze dies durch deine Gmail-Adresse
        mailSender.setUsername("ferry.juraczka@gmail.com");

        // Holt das Passwort aus der Umgebungsvariable, die wir in IntelliJ gesetzt haben
        String password = System.getenv("MAIL_PASSWORD");
        mailSender.setPassword(password);

        // 3. SMTP-Eigenschaften (Wichtig für Gmail!)
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Gmail zwingt zu TLS
        props.put("mail.debug", "true"); // Zeigt detaillierte Fehler in der Konsole

        return mailSender;
    }

    public static void main(String[] args) {
        SpringApplication.run(SunServiceApplication.class, args);
    }

}
