package com.ferry.sunservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Dieser Filter prüft bei jedem Request (außer Login), ob ein gültiger
 * JWT-Token im Header mitgeschickt wurde.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // Konstruktor für Dependency Injection
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. BYPASS: Wenn die Anfrage an den Auth-Endpunkt geht, ignorieren wir den Filter.
        // Ohne dies würde der Filter versuchen, einen Token zu validieren, der beim Login noch gar nicht existiert.
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Token aus dem Header "Authorization" extrahieren
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " abschneiden

            try {
                // 3. Token validieren und Benutzernamen extrahieren
                String username = jwtService.validateTokenAndGetUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 4. Authentifizierung für Spring Security erstellen
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                    // 5. Den User im Sicherheits-Kontext von Spring setzen
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("JWT validiert für User: " + username);
                }
            } catch (Exception e) {
                // Falls der Token abgelaufen oder manipuliert ist
                System.out.println("Ungültiger oder abgelaufener JWT Token erhalten");
                // Wir werfen hier keinen Fehler, damit die SecurityConfig
                // mit einem sauberen 401/403 antworten kann.
            }
        }

        // 6. Weitergabe an den nächsten Filter in der Kette
        filterChain.doFilter(request, response);
    }
}