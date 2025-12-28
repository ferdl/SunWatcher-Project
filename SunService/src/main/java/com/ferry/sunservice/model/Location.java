package com.ferry.sunservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lat;
    private String lng;

    // Standard-Konstruktoren für JPA
    public Location() {}

    public Location(String name, String lat, String lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
}