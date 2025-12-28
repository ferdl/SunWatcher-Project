package com.ferry.sunservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // 'user' ist in SQL oft ein reserviertes Wort
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // Hier landet der verschlüsselte Hash

    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}