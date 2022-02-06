package com.controller;

import com.model.*;
import com.service.AccesoService;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    AccesoService service;

    @GetMapping(value = "/personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> altaPersona(@PathVariable("nombre") String nombre,
            @PathVariable("email") String email,
            @PathVariable("edad") int edad) {

        Persona persona = new Persona(nombre, email, edad);
        CompletableFuture<List<Persona>> resultado = service.servicio(persona);
        for (int i = 0; i < 50; i++) {
            System.out.println("Esperando");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
               
            }
        }
        try {
            return resultado.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null; 
        }
    }
}
