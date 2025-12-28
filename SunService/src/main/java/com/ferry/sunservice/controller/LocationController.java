package com.ferry.sunservice.controller;

import com.ferry.sunservice.model.Location;
import com.ferry.sunservice.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class LocationController {

    @Autowired
    private LocationRepository repository;

    @GetMapping
    public List<Location> getAllLocations() {
        return repository.findAll();
    }

    // NEU: Methode zum Speichern eines neuen Ortes
    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody Location newLocation) {
        Location savedLocation = repository.save(newLocation);
        System.out.println("Neuer Ort gespeichert: " + savedLocation.getName());
        return ResponseEntity.ok(savedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        repository.deleteById(id);
        System.out.println("Ort mit ID " + id + " wurde gelöscht.");
        return ResponseEntity.noContent().build();
    }
}