package com.ferry.sunservice.dto;

public class SunDto {
    private String sunrise;
    private String sunset;
    private String dayLength;

    // Konstruktor, Getter und Setter
    public SunDto(String sunrise, String sunset, String dayLength) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.dayLength = dayLength;
    }
    // WICHTIG: Ohne diese Getter bleibt das Ergebnis {}
    public String getSunrise() { return sunrise; }
    public String getSunset() { return sunset; }
    public String getDayLength() { return dayLength; }
}