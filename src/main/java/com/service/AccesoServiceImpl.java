package com.service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import com.model.Persona;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccesoServiceImpl implements AccesoService {

    @Autowired
    RestTemplate template;

    private String urlBase = "http://localhost:8080";

    @Async
    @Override
    public CompletableFuture<java.util.List<Persona>> servicio(Persona persona) {
        template.postForLocation(urlBase + "/contactos", persona);
        Persona[] personas = template.getForObject(urlBase + "/contactos", Persona[].class);
        return CompletableFuture.completedFuture(Arrays.asList(personas));
    }

}
