package com.ferry.sunservice.controller;

import com.ferry.sunservice.service.SunService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {
    // Logger initialisieren
    private static final Logger logger = LoggerFactory.getLogger(GalleryController.class);
    private final SunService.ImageService imageService;

    public GalleryController(SunService.ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/test-alive")
    public ResponseEntity<String> testAlive() {
        return ResponseEntity.ok("Der Controller lebt und ist erreichbar!");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.info("Upload-Anfrage für Datei: {}", file.getOriginalFilename());
        try {
            imageService.saveAndScale(file);
            logger.info("Datei erfolgreich verarbeitet und gespeichert: {}", file.getOriginalFilename());
            return ResponseEntity.ok("Bild erfolgreich hochgeladen und optimiert.");
        } catch (IOException e) {
            logger.error("Fehler beim Upload von {}: ", file.getOriginalFilename(), e);
            return ResponseEntity.internalServerError().body("Fehler beim Speichern.");
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getImages() {
        logger.info("Anfrage erhalten: Liste der Galeriebilder (sortiert) abrufen.");
        java.nio.file.Path path = java.nio.file.Paths.get("/app/images/gallery");

        if (!java.nio.file.Files.exists(path)) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(path)) {
            List<String> fileNames = stream
                    .filter(file -> !java.nio.file.Files.isDirectory(file))
                    .filter(file -> {
                        String name = file.getFileName().toString().toLowerCase();
                        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
                    })
                    // HIER passiert die Sortierung:
                    .sorted((file1, file2) -> {
                        try {
                            // Wir vergleichen das Erstelldatum/Änderungsdatum absteigend (neueste zuerst)
                            return java.nio.file.Files.getLastModifiedTime(file2)
                                    .compareTo(java.nio.file.Files.getLastModifiedTime(file1));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .map(java.nio.file.Path::getFileName)
                    .map(java.nio.file.Path::toString)
                    .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(fileNames);
        } catch (java.io.IOException e) {
            logger.error("Fehler beim Einlesen der Galerie: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/images/{*filename}")
    public ResponseEntity<?> deleteImage(HttpServletRequest request) {
        // Wir extrahieren den Dateinamen manuell aus dem Pfad
        String fullPath = request.getRequestURI();
        // fullPath ist z.B. /api/gallery/images/mein-bild.jpg
        String filename = fullPath.substring(fullPath.lastIndexOf("/") + 1);

        logger.info("MANUELLES MATCHING: Lösch-Anfrage für Datei: {}", filename);

        try {
            Path root = Paths.get("/app/images/gallery/");
            Path filePath = root.resolve(filename).normalize();

            // Sicherheits-Check gegen ../ Angriffe
            if (!filePath.startsWith(root)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ungültiger Pfad!");
            }

            File file = filePath.toFile();
            if (file.exists() && file.delete()) {
                logger.info("Datei erfolgreich gelöscht: {}", filePath);
                return ResponseEntity.ok().body("{\"message\": \"Bild erfolgreich gelöscht\"}");
            } else {
                logger.warn("Datei nicht gefunden oder nicht löschbar: {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Datei nicht gefunden");
            }
        } catch (Exception e) {
            logger.error("Fehler beim Löschen: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
