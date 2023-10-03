package com.example.directory.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Controller
public class CountryController {

    @GetMapping("/countries")
    public ResponseEntity<String> getCountriesJson() throws IOException {
        Resource resource = new ClassPathResource("static/countries-list.json");
        InputStream inputStream = resource.getInputStream();
        String json = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        return ResponseEntity.ok(json);
    }
}
