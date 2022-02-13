package com.controller;

import com.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    private HttpHeaders headers = new HttpHeaders();
    private String user = "admin";
    private String pwd = "admin";
    private String token;

    @PostConstruct()
    // Al activar el controller solicito el token asociado al usuario al servicio de
    // contactos
    // en caso de que este correctamente logeado
    public void autenticar() {
        token = template.postForObject(urlBase + "/login?user=" + user + "&pwd=" + pwd, null, String.class);
        System.out.println("Token Generado " + token);
        // declaro la cabecera con el token para pasarlo como parametro en el servicio
        // exchange
        headers.add("Authorization", "Bearer " + token);
        System.out.println(headers.getValuesAsList("Authorization"));
    }

    @GetMapping(value = "/personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Persona>> altaPersona(@PathVariable("nombre") String nombre,
            @PathVariable("email") String email,
            @PathVariable("edad") int edad) {

        // Almaceno la persona-contacto a dar de alta
        Persona persona = new Persona(nombre, email, edad);

        try {
            // Le paso al servicio PosMapping el Entity con el objeto a insertar y la
            // cabecera
            template
                    .exchange(urlBase + "/contactos", HttpMethod.POST, new HttpEntity<Persona>(persona, headers),
                            String.class);

            System.out.println("Creación de persona-contacto");
            // solicito la lista de personas (co el objeto reciente creado)
            Persona[] personas = template
                    .exchange(urlBase + "/contactos", HttpMethod.GET, new HttpEntity<>(headers), Persona[].class)
                    .getBody();
            System.out.println("Entrega de lista Personas");
            return new ResponseEntity<List<Persona>>(Arrays.asList(personas), HttpStatus.OK);

        } catch (HttpStatusCodeException exception) {
            // si hubo error en la llamada al microservicio, enviamos a nuestro cliente
            // final
            // una cabecera con el mensaje de error, una lista vacia de personas en el
            // cuerpo
            headers.add("error", exception.getResponseBodyAsString());
            System.out.println("Error en la llamada al servicio");
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
        Persona[] personas = template
                .exchange(urlBase + "/contactos", HttpMethod.GET, new HttpEntity<>(headers), Persona[].class)
                .getBody();
        return Arrays.stream(personas)
                .filter(p -> p.getEdad() >= edad1 && p.getEdad() <= edad2)
                .collect(Collectors.toList());
    }
}
