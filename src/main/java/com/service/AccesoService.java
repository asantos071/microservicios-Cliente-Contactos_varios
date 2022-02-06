package com.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.model.Persona;

public interface AccesoService {

    CompletableFuture<List<Persona>> servicio(Persona persona);

}
