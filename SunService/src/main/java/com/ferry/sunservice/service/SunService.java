package com.ferry.sunservice.service;

import com.ferry.sunservice.dto.SunDto;
import com.ferry.sunservice.model.SunResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        // 1. Hol dir die Rohdaten (deine bisherige Methode)
        SunResponse raw = getSunData(lat, lng, date);

        // 2. Formatiere die Zeiten
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedSunrise = ZonedDateTime.parse(raw.getResults().getSunrise())
                .withZoneSameInstant(java.time.ZoneId.of("Europe/Berlin"))
                .format(formatter);
        String formattedSunset = ZonedDateTime.parse(raw.getResults().getSunset())
                .withZoneSameInstant(java.time.ZoneId.of("Europe/Berlin"))
                .format(formatter);

        // 3. Gib das schöne DTO zurück
        return new SunDto(formattedSunrise, formattedSunset, raw.getResults().getDay_length());
    }
}