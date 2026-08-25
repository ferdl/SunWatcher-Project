package com.ferry.sunservice.controller;

import com.ferry.sunservice.service.SunService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

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
        logger.info("Anfrage erhalten: Liste der Galeriebilder abrufen.");
        // Greift jetzt direkt auf die rekursive Logik aus dem ImageService zu:
        List<String> fileNames = imageService.getImageFilenames();
        return ResponseEntity.ok(fileNames);
    }

    @DeleteMapping("/images/{*filename}")
    public ResponseEntity<?> deleteImage(HttpServletRequest request) {
        String fullPath = request.getRequestURI();
        String filename = fullPath.substring(fullPath.lastIndexOf("/") + 1);

        logger.info("MANUELLES MATCHING: Lösch-Anfrage für Datei: {}", filename);

        try {
            Path root = Paths.get("/app/images/gallery/");
            Path filePath = root.resolve(filename).normalize();

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