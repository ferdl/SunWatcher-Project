package com.ferry.sunservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SunResults {
    private String sunrise;
    private String sunset;
    private String day_length;

    // Getter und Setter
    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }
    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }
    public String getDay_length() { return formatDayLength(day_length); }
    public void setDay_length(String day_length) { this.day_length = day_length; }

    private String formatDayLength(String secondsStr) {
        try {
            long totalSeconds = Long.parseLong(secondsStr);

            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;

            return String.format("%d Stunden und %d Minuten", hours, minutes);
        } catch (NumberFormatException e) {
            return "Unbekannte Länge";
        }
    }
}