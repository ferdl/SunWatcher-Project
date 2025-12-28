package com.ferry.sunservice.model;

public class SunResponse {
    private SunResults results;
    private String status;

    // Getter und Setter
    public SunResults getResults() { return results; }
    public void setResults(SunResults results) { this.results = results; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}