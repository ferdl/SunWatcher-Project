package com.ferry.sunservice.dto;

/**
 * DTO (Data Transfer Object) für die Kontaktanfragen vom Frontend.
 * Spring Boot nutzt diese Klasse, um das eingehende JSON automatisch zu konvertieren.
 */
public class ContactRequest {

    private String name;
    private String email;
    private String message;

    // Ein leerer Standard-Konstruktor ist zwingend erforderlich für Jackson (JSON-Parser)
    public ContactRequest() {
    }

    // Konstruktor für bequemeres Erstellen (optional)
    public ContactRequest(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
    }

    // --- Getter und Setter ---
    // Diese sind notwendig, damit Spring die Daten in die Felder schreiben kann.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // toString Methode für einfacheres Logging (optional)
    @Override
    public String toString() {
        return "ContactRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", message='[MESSAGE_CONTENT]'" +
                '}';
    }
}