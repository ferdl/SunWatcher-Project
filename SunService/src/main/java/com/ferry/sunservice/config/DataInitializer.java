package com.ferry.sunservice.config;

import com.ferry.sunservice.model.Location;
import com.ferry.sunservice.model.User;
import com.ferry.sunservice.repository.LocationRepository;
import com.ferry.sunservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(LocationRepository repository, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            // Nur wenn noch keine Orte da sind, die Standard-Orte anlegen
            if (repository.count() == 0) {
                repository.save(new Location("Wien", "48.2082", "16.3738"));
                repository.save(new Location("Berlin", "52.5200", "13.4050"));
                System.out.println("Standard-Orte wurden initial angelegt.");
            }

            // Das Gleiche für den Admin-User
            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(new User("admin", encoder.encode("password123")));
            }
        };
    }
}