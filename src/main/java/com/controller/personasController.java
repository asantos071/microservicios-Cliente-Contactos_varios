package com.controller;

import com.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class personasController {

    @Autowired
    RestTemplate template;

    private String urlBase = "http://localhost:8080";

    @GetMapping(value = "/personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> altaPersona(@PathVariable("nombre") String nombre,
            @PathVariable("email") String email,
            @PathVariable("edad") int edad) {

        Persona persona = new Persona(nombre, email, edad);
        template.postForLocation(urlBase + "/contactos", persona); 
        Persona[] personas = template.getForObject(urlBase + "/contactos", Persona[].class);
        return Arrays.asList(personas);

    }

    @GetMapping(value="/perosnas/{edad1}/{edad2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> buscarEdades(@PathVariable("edad1") int edad1,
    @PathVariable("edad2") int edad2){
        Persona[] personas = template.getForObject(urlBase+ "/contactos", Persona[].class);
        return Arrays.stream(personas)
        .filter(p-> p.getEdad()>= edad1 && p.getEdad()<= edad2)
        .collect(Collectors.toList());
    }
}
