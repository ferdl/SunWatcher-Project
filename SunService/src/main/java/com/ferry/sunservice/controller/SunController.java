package com.ferry.sunservice.controller;

import com.ferry.sunservice.dto.SunDto;
import com.ferry.sunservice.service.SunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sun")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class SunController {

    @Autowired
    private SunService sunService;

    // Dein lokaler Endpunkt: http://localhost:8080/sun?lat=...&lng=...
    @GetMapping
    public SunDto getSun(@RequestParam String lat, @RequestParam String lng, @RequestParam(name = "date", defaultValue = "today") String date) {
        return sunService.getFormattedSunData(lat, lng, date);
    }
}