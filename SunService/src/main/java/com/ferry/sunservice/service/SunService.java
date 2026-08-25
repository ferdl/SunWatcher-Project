package com.ferry.sunservice.service;

import com.ferry.sunservice.dto.SunDto;
import com.ferry.sunservice.model.SunResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SunService {

    public SunResponse getSunData(String lat, String lng, String date) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.sunrise-sunset.org")
                .path("/json")
                .queryParam("lat", lat)
                .queryParam("lng", lng)
                .queryParam("date", date)
                .queryParam("formatted", 0)
                .build()
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();

        // Ruft die API auf und wandelt JSON automatisch in unsere Java-Klasse um
        return restTemplate.getForObject(url, SunResponse.class);
    }

    public SunDto getFormattedSunData(String lat, String lng, String date) {
        // 1. Hol dir die Rohdaten
        SunResponse raw = getSunData(lat, lng, date);

        // 2. Formatiere die Zeiten
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedSunrise = ZonedDateTime.parse(raw.getResults().getSunrise())
                .withZoneSameInstant(java.time.ZoneId.of("Europe/Berlin"))
                .format(formatter);
        String formattedSunset = ZonedDateTime.parse(raw.getResults().getSunset())
                .withZoneSameInstant(java.time.ZoneId.of("Europe/Berlin"))
                .format(formatter);

        // 3. Gib das DTO zurück
        return new SunDto(formattedSunrise, formattedSunset, raw.getResults().getDay_length());
    }

    @Service
    public static class ImageService {
        private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
        private final String uploadDir = "/app/images/gallery/";

        public List<String> getImageFilenames() {
            Path rootPath = Paths.get(uploadDir);
            if (!Files.exists(rootPath) || !Files.isDirectory(rootPath)) {
                logger.warn("Galerie-Verzeichnis nicht gefunden: {}", uploadDir);
                return Collections.emptyList();
            }

            // Files.walk liest auch alle Unterordner (babybauch, familie, etc.) rekursiv ein
            try (Stream<Path> stream = Files.walk(rootPath)) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(path -> {
                            String lower = path.getFileName().toString().toLowerCase();
                            return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".webp");
                        })
                        .map(rootPath::relativize)
                        .map(Path::toString)
                        .map(p -> p.replace("\\", "/")) // Für Windows-Kompatibilität Pfadtrenner vereinheitlichen
                        .collect(Collectors.toList());
            } catch (Exception e) {
                logger.error("Fehler beim Lesen der Galeriebilder aus allen Ordnern: {}", e.getMessage(), e);
                return Collections.emptyList();
            }
        }

        public void saveAndScale(MultipartFile file) throws IOException {
            File targetFile = new File(uploadDir + file.getOriginalFilename());

            // Nutzt Thumbnailator zum Skalieren und Komprimieren
            Thumbnails.of(file.getInputStream())
                    .size(1200, 1200)       // Maximale Breite/Höhe
                    .outputQuality(0.8)     // 80% Qualität
                    .toFile(targetFile);
            logger.debug("Bild auf {} skaliert und gespeichert.", targetFile.getAbsolutePath());
        }
    }
}