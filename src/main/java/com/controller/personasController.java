package com.controller;

import com.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@RestController
public class personasController {

    @Autowired
    RestTemplate template;

    private String urlBase = "http://localhost:8080";

    @GetMapping(value = "/personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Persona>> altaPersona(@PathVariable("nombre") String nombre,
            @PathVariable("email") String email,
            @PathVariable("edad") int edad) {
        Persona persona = new Persona(nombre, email, edad);
        try {
            template.postForLocation(urlBase + "/contactos", persona);
            Persona[] personas = template.getForObject(urlBase + "/contactos", Persona[].class);
            return new ResponseEntity<List<Persona>>(Arrays.asList(personas), HttpStatus.OK);

        } catch (HttpStatusCodeException exception) {
            // si hubo error en la llamada al microservicio, enviamos a nuestro cliente
            // final
            // una cabecera con el mensaje de error, una lista vacia de personas en el
            // cuerpo
            HttpHeaders headers = new HttpHeaders();
            headers.add("error", exception.getResponseBodyAsString());
            return new ResponseEntity<List<Persona>>(new ArrayList<Persona>(), headers, exception.getStatusCode());

        }

    }

    @PostMapping(value = "/personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> altaPersonaPost(@PathVariable("nombre") String nombre,
            @PathVariable("email") String email,
            @PathVariable("edad") int edad) {

        Persona persona = new Persona(nombre, email, edad);
        String resp = template.postForObject(urlBase + "/contactos", persona, String.class);
        Persona[] personas = null;
        if (String.valueOf(resp) == "n/a") {
            personas = template.getForObject(urlBase + "/contactos", Persona[].class);
            return Arrays.asList(personas);
        } else {
            return null;
        }
    }

    @GetMapping(value = "/personas/{edad1}/{edad2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> buscarEdades(@PathVariable("edad1") int edad1,
            @PathVariable("edad2") int edad2) {
        Persona[] personas = template.getForObject(urlBase + "/contactos", Persona[].class);
        return Arrays.stream(personas)
                .filter(p -> p.getEdad() >= edad1 && p.getEdad() <= edad2)
                .collect(Collectors.toList());
    }
}
